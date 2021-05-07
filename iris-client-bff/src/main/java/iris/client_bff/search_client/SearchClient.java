package iris.client_bff.search_client;

import iris.client_bff.search_client.dto.LocationInformation;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.search_client.dto.LocationList;

public interface SearchClient {
  LocationList search(String keyword) throws IRISSearchException;
  LocationInformation findByProviderIdAndLocationId(String providerId, String locationId) throws IRISSearchException;
}
