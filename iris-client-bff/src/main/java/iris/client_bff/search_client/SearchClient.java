package iris.client_bff.search_client;

import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.search_client.web.dto.LocationInformation;
import iris.client_bff.search_client.web.dto.LocationQueryResult;

import org.springframework.data.domain.Pageable;

public interface SearchClient {
	LocationQueryResult search(String keyword, Pageable pageable) throws IRISSearchException;

	LocationInformation findByProviderIdAndLocationId(String providerId, String locationId) throws IRISSearchException;
}
