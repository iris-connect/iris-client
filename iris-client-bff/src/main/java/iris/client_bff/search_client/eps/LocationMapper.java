package iris.client_bff.search_client.eps;

import iris.client_bff.events.model.Location;
import iris.client_bff.events.model.Location.LocationIdentifier;
import iris.client_bff.search_client.web.dto.LocationInformation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {

	private static final ModelMapper mapper = new ModelMapper();

	public static Location map(LocationInformation locationInformation) {
		var location = mapper.map(locationInformation, Location.class);
		location.setId(LocationIdentifier.random());

		if (location.getLocationId() == null) {
			location.setLocationId(locationInformation.getId());
		}

		if (location.getProviderId() == null) {
			location.setProviderId(locationInformation.getProviderId());
		}
		return location;
	}

}
