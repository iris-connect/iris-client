package iris.client_bff.search_client.eps;

import static iris.client_bff.core.log.LogHelper.*;

import iris.client_bff.config.BackendServiceProperties;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.eps.dto.IdSearch;
import iris.client_bff.search_client.eps.dto.KeywordSearch;
import iris.client_bff.search_client.eps.dto.PageableDto;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.search_client.web.dto.LocationInformation;
import iris.client_bff.search_client.web.dto.LocationQueryResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Slf4j
@Service
@AllArgsConstructor
public class EPSSearchClient implements SearchClient {

	private BackendServiceProperties config;

	private final JsonRpcHttpClient epsRpcClient;

	@Override
	public LocationInformation findByProviderIdAndLocationId(String providerId, String locationId)
			throws IRISSearchException {

		var methodName = config.getEndpoint() + ".getLocationDetails";

		var payload = IdSearch.builder()
				.providerId(providerId)
				.locationId(locationId)
				.build();

		log.info(LOCATION_DETAILS);

		try {
			return epsRpcClient.invoke(methodName, payload, LocationInformation.class);
		} catch (Throwable t) {
			throw new IRISSearchException(methodName, t);
		}
	}

	@Override
	public LocationQueryResult search(String keyword, Pageable pageable) throws IRISSearchException {
		PageableDto pageableDto = PageableDto.builder().build();
		if (pageable != null && pageable.isPaged()) {
			pageableDto.setPage(pageable.getPageNumber());
			pageableDto.setSize(pageable.getPageSize());

			if (pageable.getSort().isSorted()) {
				// We only want to sort by one single property
				Optional<Sort.Order> order = pageable.getSort().stream().findFirst();
				pageableDto.setSortBy(order.get().getProperty());
				pageableDto.setDirection(order.get().getDirection());
			}
		}

		KeywordSearch search = KeywordSearch.builder()
				.searchKeyword(keyword)
				.pageable(pageableDto).build();
		var methodName = config.getEndpoint() + ".searchForLocation";
		try {
			log.info(LOCATION_SEARCH);
			return epsRpcClient.invoke(methodName, search, LocationQueryResult.class);
		} catch (Throwable t) {
			throw new IRISSearchException(methodName, t);
		}
	}
}
