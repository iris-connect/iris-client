package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@ToString
public class ContactsAndEventsDataProvider {

	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
}
