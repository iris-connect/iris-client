package iris.client_bff.config;

import iris.client_bff.IrisClientBffApplication;
import iris.client_bff.core.IdWithUuid;
import iris.client_bff.core.converter.PrimitivesToIdWithUuidConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.de.GermanNormalizationFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.bridge.IdentifierBridge;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeFromDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeToDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
@RequiredArgsConstructor
class HibernateSearchConfig {

	private final @NonNull EntityManagerFactory emf;
	private SearchSession session;

	@Bean
	SearchSession getSearchSession() {
		return Search.session(emf.createEntityManager());
	}

	@PostConstruct
	void initialize() throws InterruptedException {

		var classes = searchClassesWithAnnotationIndexed();

		MassIndexer indexer = createAndGetSearchSession().massIndexer(classes).threadsToLoadObjects(2);
		try {
			indexer.startAndWait();
		} catch (InterruptedException e) {
			log.error("Hibernate Search indexer was interrupted!", e);
			throw e;
		}
	}

	private List<? extends Class<?>> searchClassesWithAnnotationIndexed() {

		var scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Indexed.class));

		var components = scanner.findCandidateComponents(IrisClientBffApplication.class.getPackageName());

		return components.stream()
				.filter(ScannedGenericBeanDefinition.class::isInstance)
				.map(ScannedGenericBeanDefinition.class::cast)
				.map(AbstractBeanDefinition::getBeanClassName)
				.map(this::loadClass)
				.toList();
	}

	@SneakyThrows
	private Class<?> loadClass(String clazz) {
		return getClass().getClassLoader().loadClass(clazz);
	}

	SearchSession createAndGetSearchSession() {

		return session == null
				? session = Search.session(emf.createEntityManager())
				: session;
	}

	@Component("DocumentAnalysisConfigurer")
	static class DocumentAnalysisConfigurer implements LuceneAnalysisConfigurer {

		@Override
		public void configure(LuceneAnalysisConfigurationContext context) {

			context.analyzer("german").custom()
					.tokenizer(StandardTokenizerFactory.class)
					.tokenFilter(LowerCaseFilterFactory.class)
					.tokenFilter(GermanNormalizationFilterFactory.class)
					.tokenFilter(SnowballPorterFilterFactory.class)
					.param("language", "German2")
					.tokenFilter(ASCIIFoldingFilterFactory.class);

			context.normalizer("german").custom()
					.tokenFilter(LowerCaseFilterFactory.class)
					.tokenFilter(GermanNormalizationFilterFactory.class)
					.tokenFilter(SnowballPorterFilterFactory.class)
					.param("language", "German2")
					.tokenFilter(ASCIIFoldingFilterFactory.class);
		}
	}

	@Component("SearchMappingConfigurer")
	@RequiredArgsConstructor
	static class SearchMappingConfigurer implements HibernateOrmSearchMappingConfigurer {

		private final @NonNull PrimitivesToIdWithUuidConverter converter;

		@Override
		public void configure(HibernateOrmMappingConfigurationContext context) {

			context.bridges().strictSubTypesOf(IdWithUuid.class).identifierBinder(idBindingContext -> {

				var idType = (Class<IdWithUuid>) idBindingContext.bridgedElement().rawType();
				idBindingContext.bridge(idType, IdWithUuidIdentifierBridge.of(converter, idType));
			});

			context.bridges().strictSubTypesOf(IdWithUuid.class).valueBinder(valueBindingContext -> {

				var valueType = (Class<IdWithUuid>) valueBindingContext.bridgedElement().rawType();
				valueBindingContext.bridge(valueType, IdWithUuidValueBridge.of(converter, valueType));
			});
		}
	}

	/**
	 * @author Jens Kutzsche
	 */
	@RequiredArgsConstructor(staticName = "of")
	static class IdWithUuidIdentifierBridge<T extends IdWithUuid> implements IdentifierBridge<T> {

		private final PrimitivesToIdWithUuidConverter converter;
		private final Class<T> clazz;

		@Override
		public String toDocumentIdentifier(T value, IdentifierBridgeToDocumentIdentifierContext context) {
			return value == null ? null : value.toString();
		}

		@Override
		public T fromDocumentIdentifier(String value,
				IdentifierBridgeFromDocumentIdentifierContext context) {
			return (T) converter.convert(value, TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(clazz));
		}
	}

	/**
	 * @author Jens Kutzsche
	 */
	@RequiredArgsConstructor(staticName = "of")
	static class IdWithUuidValueBridge<T extends IdWithUuid> implements ValueBridge<T, String> {

		private final PrimitivesToIdWithUuidConverter converter;
		private final Class<T> clazz;

		@Override
		public String toIndexedValue(T value, ValueBridgeToIndexedValueContext context) {
			return value == null ? null : value.toString();
		}

		@Override
		public T fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
			return (T) converter.convert(value, TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(clazz));
		}
	}
}
