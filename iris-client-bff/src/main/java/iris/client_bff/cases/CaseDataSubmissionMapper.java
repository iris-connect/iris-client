package iris.client_bff.cases;

import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.ContactPerson;
import iris.client_bff.cases.eps.dto.Event;
import iris.client_bff.cases.model.CaseEvent;
import iris.client_bff.cases.model.Contact;
import iris.client_bff.config.MapStructCentralConfig;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructCentralConfig.class)
public interface CaseDataSubmissionMapper {

	@Mapping(target = "workplaceName", source = "workPlace.name")
	@Mapping(target = "workplacePhone", source = "workPlace.phone")
	@Mapping(target = "workplaceAddress", source = "workPlace.address")
	@Mapping(target = "workplacePointOfContact", source = "workPlace.pointOfContact")
	@Mapping(target = ".", source = "contactInformation")
	Contact fromContactPersonDto(ContactPerson contactPerson);

	iris.client_bff.cases.model.CaseDataProvider fromCaseDataProviderDto(CaseDataProvider caseDataProvider);

	CaseEvent fromEventDto(Event event);
}
