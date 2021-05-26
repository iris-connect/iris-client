package iris.client_bff.search_client.eps;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.SearchClientProperties;
import iris.client_bff.search_client.eps.dto.IdSearch;
import iris.client_bff.search_client.eps.dto.KeywordSearch;
import iris.client_bff.search_client.eps.dto.PageableDto;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.search_client.web.dto.LocationInformation;
import iris.client_bff.search_client.web.dto.LocationQueryResult;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EPSSearchClient implements SearchClient {

	private SearchClientProperties config;

	private final JsonRpcHttpClient rpcClient;

	public LocationInformation findByProviderIdAndLocationId(String providerId, String locationId)
			throws IRISSearchException {

		var payload = IdSearch.builder().providerId(providerId).locationId(locationId).build();

		LocationInformation locationInformation;
		try {
			var methodName = config.getEndpoint() + ".getLocationDetails";
			locationInformation = rpcClient.invoke(methodName, payload, LocationInformation.class);
		} catch (Throwable t) {
			throw new IRISSearchException(t);
		}

		// TODO check: why do we need to do this?

		if (StringUtils.isEmpty(locationInformation.getProviderId())) {
			locationInformation.setProviderId(providerId);
		}

		if (StringUtils.isEmpty(locationInformation.getId())) {
			locationInformation.setId(locationId);
		}

		return locationInformation;
	}

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

		KeywordSearch search = KeywordSearch.builder().searchKeyword(keyword).pageable(pageableDto).build();
		try {
			var methodName = config.getEndpoint() + ".searchForLocation";
			return rpcClient.invoke(methodName, search, LocationQueryResult.class);
		} catch (Throwable t) {
			throw new IRISSearchException(t);
		}
	}
}
