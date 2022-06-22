package iris.client_bff.cases.web.submission_dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.api.dto.Address;
import iris.client_bff.core.model.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Extended person data type for contact persons who had contact with the queried person during the queried time.
 */
@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ContactPerson {

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	@Builder.Default
	private Sex sex = Sex.UNKNOWN;

	private String email;

	private String phone;

	private String mobilePhone;

	private Address address;

	private ContactPersonAllOfWorkPlace workPlace;

	private ContactPersonAllOfContactInformation contactInformation;

}
