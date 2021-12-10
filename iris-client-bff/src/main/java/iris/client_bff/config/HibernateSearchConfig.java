package iris.client_bff.config;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.model.Location.LocationIdentifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeFromDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeToDocumentIdentifierContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

		MassIndexer indexer = createAndGetSearchSession().massIndexer(CaseDataRequest.class, EventDataRequest.class)
				.threadsToLoadObjects(2);
		try {
			indexer.startAndWait();
		} catch (InterruptedException e) {
			log.error("Hibernate Search indexer was interrupted!", e);
			throw e;
		}
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
		}
	}
}
