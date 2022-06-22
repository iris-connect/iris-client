package iris.client_bff.core.api.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.model.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * A person base class.
 */
@Data
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Person {

	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	@Builder.Default
	private Sex sex = Sex.UNKNOWN;
	private String email;
	private String phone;
	private String mobilePhone;
	private Address address;
}
