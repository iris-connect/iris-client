package de.healthIMIS.iris.client.search_client;

import de.healthIMIS.iris.api.sidecarclient.model.LocationInformation;
import de.healthIMIS.iris.api.sidecarclient.model.LocationList;
import de.healthIMIS.iris.client.data_request.Location;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class SearchClient {

	private SearchClientProperties config;

	@Qualifier("search-rest")
	private RestTemplate rest;

	private final @NonNull ModelMapper mapper;

	public Location findByProviderIdAndLocationId(String providerId, String locationId) {
		var locationDto = rest.getForObject(config.getEndpoint() + "/{providerId}/{locationId}",
				LocationInformation.class,
				providerId, locationId);
		var location = mapper.map(locationDto, Location.class);
		location.setId(new Location.LocationIdentifier());
		location.setLocationId(locationDto.getId());
		// Todo: Why is it null
		location.setProviderId(providerId);
		return location;
	}

	public LocationList search(String keyword) {
		return rest.getForObject(config.getEndpoint() + "/{keyword}", LocationList.class, keyword);
	}
}
