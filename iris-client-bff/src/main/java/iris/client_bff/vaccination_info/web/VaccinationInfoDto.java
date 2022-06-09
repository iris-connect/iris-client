package iris.client_bff.vaccination_info.web;

import iris.client_bff.core.api.dto.Address;
import iris.client_bff.core.model.Sex;
import iris.client_bff.vaccination_info.VaccinationStatus;
import iris.client_bff.vaccination_info.VaccinationType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface VaccinationInfoDto {
	record VaccinationReport(
			String id,
			Facility facility,
			Instant reportedAt,
			Map<VaccinationStatus, Long> vaccinationStatusCount) {}

	record VaccinationReportDetails(
			String id,
			Facility facility,
			Instant reportedAt,
			Map<VaccinationStatus, Long> vaccinationStatusCount,
			Set<Employee> employees) {}

	record Facility(
			String name,
			Address address,
			ContactPerson contactPerson) {}

	record ContactPerson(
			String firstName,
			String lastName,
			String eMail,
			String phone) {}

	record Employee(
			String firstName,
			String lastName,
			LocalDate dateOfBirth,
			Sex sex,
			Address address,
			String phone,
			String eMail,
			VaccinationType vaccination,
			VaccinationStatus vaccinationStatus,
			String messageDataSelectId) {}

}
