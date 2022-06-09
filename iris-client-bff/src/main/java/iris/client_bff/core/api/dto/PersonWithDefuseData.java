package iris.client_bff.core.api.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.Sex;
import iris.client_bff.core.serialization.DefuseJsonString;
import iris.client_bff.core.validation.AttackDetector.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import javax.validation.Valid;

/**
 * A person base class.
 */
@Data
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class PersonWithDefuseData {

	@DefuseJsonString(maxLength = 100)
	private String firstName;

	@DefuseJsonString(maxLength = 100)
	private String lastName;

	private LocalDate dateOfBirth;

	@Builder.Default
	private Sex sex = Sex.UNKNOWN;

	@DefuseJsonString(maxLength = 100)
	private String email;

	@DefuseJsonString(maxLength = 100, payload = Phone.class)
	private String phone;

	@DefuseJsonString(maxLength = 100, payload = Phone.class)
	private String mobilePhone;

	@Valid
	private AddressWithDefuseData address;
}
