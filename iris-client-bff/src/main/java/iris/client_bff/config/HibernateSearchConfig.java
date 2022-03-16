package iris.client_bff.config;

import iris.client_bff.IrisClientBffApplication;
import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.model.Location.LocationIdentifier;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;
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
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
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
	static class SearchMappingConfigurer implements HibernateOrmSearchMappingConfigurer {
		@Override
		public void configure(HibernateOrmMappingConfigurationContext context) {

			context.bridges().exactType(CaseDataRequest.DataRequestIdentifier.class)
					.identifierBridge(new IdentifierBridge<>() {

						@Override
						public String toDocumentIdentifier(DataRequestIdentifier value,
								IdentifierBridgeToDocumentIdentifierContext context) {
							return value.toString();
						}

						@Override
						public DataRequestIdentifier fromDocumentIdentifier(String value,
								IdentifierBridgeFromDocumentIdentifierContext context) {
							return value == null ? null : DataRequestIdentifier.of(value);
						}
					});

			context.bridges().exactType(EventDataRequest.DataRequestIdentifier.class)
					.identifierBridge(new IdentifierBridge<>() {

						@Override
						public String toDocumentIdentifier(EventDataRequest.DataRequestIdentifier value,
								IdentifierBridgeToDocumentIdentifierContext context) {
							return value.toString();
						}

						@Override
						public EventDataRequest.DataRequestIdentifier fromDocumentIdentifier(String value,
								IdentifierBridgeFromDocumentIdentifierContext context) {
							return value == null ? null : EventDataRequest.DataRequestIdentifier.of(value);
						}
					});

			context.bridges().exactType(LocationIdentifier.class)
					.identifierBridge(new IdentifierBridge<>() {

						@Override
						public String toDocumentIdentifier(LocationIdentifier value,
								IdentifierBridgeToDocumentIdentifierContext context) {
							return value.toString();
						}

						@Override
						public LocationIdentifier fromDocumentIdentifier(String value,
								IdentifierBridgeFromDocumentIdentifierContext context) {
							return value == null ? null : LocationIdentifier.of(value);
						}
					});

			context.bridges().exactType(IrisMessage.IrisMessageIdentifier.class)
					.identifierBridge(new IdentifierBridge<>() {

						@Override
						public String toDocumentIdentifier(
								IrisMessage.IrisMessageIdentifier value,
								IdentifierBridgeToDocumentIdentifierContext context) {
							return value.toString();
						}

						@Override
						public IrisMessage.IrisMessageIdentifier fromDocumentIdentifier(
								String value,
								IdentifierBridgeFromDocumentIdentifierContext context) {
							return value == null ? null : IrisMessage.IrisMessageIdentifier.of(value);
						}
					});

			context.bridges().exactType(IrisMessageFolder.IrisMessageFolderIdentifier.class)
					.identifierBridge(new IdentifierBridge<>() {

						@Override
						public String toDocumentIdentifier(
								IrisMessageFolder.IrisMessageFolderIdentifier value,
								IdentifierBridgeToDocumentIdentifierContext context) {
							return value.toString();
						}

						@Override
						public IrisMessageFolder.IrisMessageFolderIdentifier fromDocumentIdentifier(
								String value,
								IdentifierBridgeFromDocumentIdentifierContext context) {
							return value == null ? null : IrisMessageFolder.IrisMessageFolderIdentifier.of(value);
						}
					});

			context.bridges().exactType(VaccinationInfo.VaccinationInfoIdentifier.class)
					.identifierBridge(new IdentifierBridge<>() {

						@Override
						public String toDocumentIdentifier(
								VaccinationInfoIdentifier value,
								IdentifierBridgeToDocumentIdentifierContext context) {
							return value.toString();
						}

						@Override
						public VaccinationInfoIdentifier fromDocumentIdentifier(
								String value,
								IdentifierBridgeFromDocumentIdentifierContext context) {
							return value == null ? null : VaccinationInfoIdentifier.of(value);
						}
					});

			context.bridges().exactType(IrisMessageFolder.IrisMessageFolderIdentifier.class)
					.valueBridge(new ValueBridge<IrisMessageFolder.IrisMessageFolderIdentifier, String>() {
						@Override
						public String toIndexedValue(
								IrisMessageFolder.IrisMessageFolderIdentifier value,
								ValueBridgeToIndexedValueContext context) {
							return value == null ? null : value.toString();
						}
					});
		}
	}
}
