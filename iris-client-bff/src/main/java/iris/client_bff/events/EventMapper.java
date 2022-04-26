package iris.client_bff.events;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.core.MetadataMapper.UserNameMetadata;
import iris.client_bff.core.web.dto.Person;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocation;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.events.web.dto.GuestListDataProvider;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructCentralConfig.class, uses = LocationMapper.class)
public interface EventMapper {

	@Mapping(target = "code", source = "id")
	@Mapping(target = "externalRequestId", source = "refId")
	@Mapping(target = "start", source = "requestStart")
	@Mapping(target = "end", source = "requestEnd")
	@Mapping(target = "requestedAt", source = "createdAt")
	@Mapping(target = "locationInformation", source = "location")
	@Mapping(target = "createdBy", qualifiedBy = UserNameMetadata.class)
	@Mapping(target = "lastModifiedBy", qualifiedBy = UserNameMetadata.class)
	DataRequestDetails toDataRequestDetailsDto(EventDataRequest eventDataRequest);

	@Mapping(target = "code", source = "id")
	@Mapping(target = "externalRequestId", source = "refId")
	@Mapping(target = "start", source = "requestStart")
	@Mapping(target = "end", source = "requestEnd")
	@Mapping(target = "requestedAt", source = "createdAt")
	@Mapping(target = "lastUpdatedAt", source = "lastModifiedAt")
	@Mapping(target = "locationInformation", source = "location")
	ExistingDataRequestClientWithLocation toExistingDataRequestClientDto(EventDataRequest eventDataRequest);

	GuestListDataProvider toGuestListDataProviderDto(
			iris.client_bff.events.model.GuestListDataProvider guestListDataProvider);

	@InheritInverseConfiguration
	iris.client_bff.events.model.GuestListDataProvider fromGuestListDataProviderDto(
			GuestListDataProvider guestListDataProvider);

	Guest toGuestDto(iris.client_bff.events.model.Guest guest);

	@InheritInverseConfiguration(name = "toGuestDto")
	iris.client_bff.events.model.Guest fromGuestDto(Guest guest);

	Person toPersonDto(iris.client_bff.events.model.Guest guest);

	Person toPersonDto(Guest guest);
}
