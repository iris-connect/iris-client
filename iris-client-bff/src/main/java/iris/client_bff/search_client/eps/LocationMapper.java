package iris.client_bff.search_client.eps;

import iris.client_bff.data_request.events.Location;
import iris.client_bff.search_client.dto.LocationInformation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {

	private static final ModelMapper mapper = new ModelMapper();

	public static Location map(LocationInformation locationInformation) {
		var location = mapper.map(locationInformation, Location.class);
		location.setId(new Location.LocationIdentifier());

		if (location.getLocationId() == null) {
			location.setLocationId(locationInformation.getId());
		}

		if (location.getProviderId() == null) {
			location.setProviderId(locationInformation.getProviderId());
		}
		return location;
	}

}
