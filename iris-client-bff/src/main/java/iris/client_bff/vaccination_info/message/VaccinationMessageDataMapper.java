package iris.client_bff.vaccination_info.message;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.web.VaccinationInfoDto;

import java.util.Set;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructCentralConfig.class, imports = UUID.class)
public interface VaccinationMessageDataMapper {

	VaccinationMessageDataPayload.Facility toFacilityPayload(VaccinationInfo.Facility facility);

	@Mapping(target = "eMail", source = "EMail")
	VaccinationMessageDataPayload.ContactPerson toContactPersonPayload(VaccinationInfo.ContactPerson contactPerson);

	Set<VaccinationMessageDataPayload.Employee> toEmployeesPayload(Set<VaccinationInfo.Employee> employees);

	@Mapping(target = "eMail", source = "email")
	@Mapping(target = "messageDataSelectId", expression = "java( UUID.randomUUID().toString() )")
	VaccinationMessageDataPayload.Employee toEmployeePayload(VaccinationInfo.Employee employee);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "reportedAt", ignore = true)
	@Mapping(target = "vaccinationStatusCount", ignore = true)
	VaccinationInfoDto.VaccinationReportDetails toVaccinationReportDetailsDto(VaccinationMessageDataPayload payload);

	Set<VaccinationInfoDto.Employee> toEmployeesDto(Set<VaccinationMessageDataPayload.Employee> employees);

	VaccinationInfo.Facility fromFacilityPayload(VaccinationMessageDataPayload.Facility facility);

	Set<VaccinationInfo.Employee> fromEmployeesPayload(Set<VaccinationMessageDataPayload.Employee> employees);

	@Mapping(target = "email", source = "eMail")
	VaccinationInfo.Employee fromEmployeePayload(VaccinationMessageDataPayload.Employee employee);
}
