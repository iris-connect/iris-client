package iris.client_bff.search_client;

import iris.client_bff.events.model.Location;
import iris.client_bff.search_client.web.dto.LocationInformation;
import iris.client_bff.search_client.web.dto.LocationList;
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

	@Qualifier("iris-rest")
	private RestTemplate rest;

	private final @NonNull ModelMapper mapper;

	public Location findByProviderIdAndLocationId(String providerId, String locationId) {
		var locationDto = rest.getForObject(config.getEndpoint() + "/{providerId}/{locationId}",
				LocationInformation.class,
				providerId, locationId);
		var location = mapper.map(locationDto, Location.class);
		location.setId(new Location.LocationIdentifier());

		if (location.getLocationId() == null) {
			if (locationDto.getId() != null) {
				location.setLocationId(locationDto.getId());
			} else {
				location.setLocationId(locationId);
			}
		}

		if (location.getProviderId() == null) {
			if (locationDto.getProviderId() != null) {
				location.setProviderId(locationDto.getProviderId());
			} else {
				location.setProviderId(providerId);
			}
		}
		return location;
	}

	public LocationList search(String keyword) {
		return rest.getForObject(config.getEndpoint() + "/{keyword}", LocationList.class, keyword);
	}
}
