package iris.client_bff.search_client.eps;

import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.SearchClientProperties;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.search_client.eps.dto.IdSearch;
import iris.client_bff.search_client.eps.dto.KeywordSearch;
import iris.client_bff.search_client.dto.LocationInformation;
import iris.client_bff.search_client.dto.LocationList;
import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Service
@AllArgsConstructor
public class EPSSearchClient implements SearchClient {

	private SearchClientProperties config;

	private final JsonRpcHttpClient rpcClient;

	public LocationInformation findByProviderIdAndLocationId(String providerId, String locationId) throws IRISSearchException {

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

	public LocationList search(String keyword) throws IRISSearchException {
		KeywordSearch search = KeywordSearch.builder().searchKeyword(keyword).build();
		try {
			var methodName = config.getEndpoint() + ".searchForLocation";
			return rpcClient.invoke(methodName, search, LocationList.class);
		} catch (Throwable t) {
			throw new IRISSearchException(t);
		}
	}
}
