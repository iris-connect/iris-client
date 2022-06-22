package iris.client_bff.events;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.web.dto.LocationInformation;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructCentralConfig.class)
public interface LocationMapper {

	@Mapping(target = "id", expression = "java(Location.LocationIdentifier.random())")
	@Mapping(target = "locationId", source = "id")
	@Mapping(target = "contactOfficialName", source = "contact.officialName")
	@Mapping(target = "contactRepresentative", source = "contact.representative")
	@Mapping(target = "contactAddressStreet", source = "contact.address.street")
	@Mapping(target = "contactAddressCity", source = "contact.address.city")
	@Mapping(target = "contactAddressZip", source = "contact.address.zip")
	@Mapping(target = "contactOwnerEmail", source = "contact.ownerEmail")
	@Mapping(target = "contactEmail", source = "contact.email")
	@Mapping(target = "contactPhone", source = "contact.phone")
	Location fromSearchClientDto(iris.client_bff.search_client.web.dto.LocationInformation locationInformation);

	@Mapping(target = "id", expression = "java(Location.LocationIdentifier.random())")
	@Mapping(target = "contactOfficialName", source = "contact.officialName")
	@Mapping(target = "contactRepresentative", source = "contact.representative")
	@Mapping(target = "contactAddressStreet", source = "contact.address.street")
	@Mapping(target = "contactAddressCity", source = "contact.address.city")
	@Mapping(target = "contactAddressZip", source = "contact.address.zip")
	@Mapping(target = "contactOwnerEmail", source = "contact.ownerEmail")
	@Mapping(target = "contactEmail", source = "contact.email")
	@Mapping(target = "contactPhone", source = "contact.phone")
	Location fromEventsDto(LocationInformation locationInformation);

	@InheritInverseConfiguration(name = "fromEventsDto")
	LocationInformation toEventsDto(Location location);
}
