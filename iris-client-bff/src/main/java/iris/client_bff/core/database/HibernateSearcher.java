package iris.client_bff.core.database;

import lombok.RequiredArgsConstructor;

import java.util.function.UnaryOperator;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
public class HibernateSearcher {

	private final SearchSession searchSession;

	public <T> SearchResult<T> search(String search, Pageable pageable, String[] fields,
			UnaryOperator<BooleanPredicateClausesStep<?>> searchInterceptor, Class<T> type) {

		return searchSession.search(type)
				.where(f -> createQuery(search, fields, searchInterceptor, f))
				.sort(f -> f.composite(b -> {
					if (pageable != null) {
						pageable.getSort().forEach(s -> {
							b.add(f.field(s.getProperty()).order(s.isAscending() ? SortOrder.ASC : SortOrder.DESC));
						});
					}
				}))
				.fetch((int) pageable.getOffset(),
						pageable.getPageSize());
	}

	private PredicateFinalStep createQuery(String keyword, String[] fields,
			UnaryOperator<BooleanPredicateClausesStep<?>> searchInterceptor, SearchPredicateFactory f) {

		var boolPred = f.bool();

		for (var keywordPart : StringUtils.split(keyword)) {
			boolPred = boolPred.must(f2 -> createQueryPart(keywordPart, fields, f2));
		}

		return searchInterceptor.apply(boolPred);
	}

	private PredicateFinalStep createQueryPart(String keyword, String[] fields, SearchPredicateFactory f) {

		return f.bool()
				.should(f2 -> f2.match()
						.fields(fields)
						.matching(keyword)
						.fuzzy())
				.should(f2 -> f2.wildcard()
						.fields(fields)
						.matching(String.format("*%s*", keyword)));
	}
}
