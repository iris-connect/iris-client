package iris.client_bff.vaccination_info.message;

import iris.client_bff.core.Sex;
import iris.client_bff.core.serialization.DefuseJsonString;
import iris.client_bff.core.validation.AttackDetector;
import iris.client_bff.core.web.dto.AddressWithDefuseData;
import iris.client_bff.vaccination_info.VaccinationStatus;
import iris.client_bff.vaccination_info.VaccinationType;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

// @TODO: test to verify that values are defused!

@Data
public class VaccinationMessageDataPayload {

	private Facility facility;
	private Set<Employee> employees;

	record Facility(
			@NotBlank @DefuseJsonString(maxLength = 100) String name,
			@NotNull AddressWithDefuseData address,
			@NotNull ContactPerson contactPerson) {}

	record ContactPerson(
			@DefuseJsonString(maxLength = 100) String firstName,
			@DefuseJsonString(maxLength = 100) String lastName,
			@DefuseJsonString(maxLength = 100) String eMail,
			@DefuseJsonString(maxLength = 100, payload = AttackDetector.Phone.class) String phone) {}

	record Employee(
			@DefuseJsonString(maxLength = 100) String firstName,
			@DefuseJsonString(maxLength = 100) String lastName,
			@Past LocalDate dateOfBirth,
			@NotNull Sex sex,
			@NotNull AddressWithDefuseData address,
			@DefuseJsonString(maxLength = 100, payload = AttackDetector.Phone.class) String phone,
			@DefuseJsonString(maxLength = 100) String eMail,
			@NotNull VaccinationType vaccination,
			@NotNull VaccinationStatus vaccinationStatus,
			@NotBlank String messageDataSelectId) {}
}
