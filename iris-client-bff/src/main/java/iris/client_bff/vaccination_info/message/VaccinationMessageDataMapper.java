package iris.client_bff.vaccination_info.message;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.web.VaccinationInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.UUID;

@Mapper(config = MapStructCentralConfig.class, imports = UUID.class)
public interface VaccinationMessageDataMapper {

	VaccinationMessageDataPayload.Facility toPayloadFacility(VaccinationInfo.Facility facility);
	@Mapping(target = "eMail", source = "EMail")
	VaccinationMessageDataPayload.ContactPerson toPayloadContactPerson(VaccinationInfo.ContactPerson contactPerson);
	Set<VaccinationMessageDataPayload.Employee> toPayloadEmployees(Set<VaccinationInfo.Employee> employees);
	@Mapping(target = "eMail", source = "email")
	@Mapping(target="messageDataSelectId", expression = "java( UUID.randomUUID().toString() )")
	VaccinationMessageDataPayload.Employee toPayloadEmployee(VaccinationInfo.Employee employee);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "reportedAt", ignore = true)
	@Mapping(target = "vaccinationStatusCount", ignore = true)
	VaccinationInfoDto.VaccinationReportDetails toWebDetails(VaccinationMessageDataPayload payload);
	VaccinationInfoDto.Employee toWebEmployee(VaccinationMessageDataPayload.Employee employee);

	VaccinationInfo.Facility toFacility(VaccinationMessageDataPayload.Facility facility);
	Set<VaccinationInfo.Employee> toEmployees(Set<VaccinationMessageDataPayload.Employee> employees);
	@Mapping(target = "email", source = "eMail")
	VaccinationInfo.Employee toEmployee(VaccinationMessageDataPayload.Employee employee);
}
