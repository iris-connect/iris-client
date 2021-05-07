package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@ToString
public class Guest {

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	@Builder.Default
	private Sex sex = Sex.UNKNOWN;

	private String email;

	private String phone;

	private String mobilePhone;

	private Address address;

	private GuestAllOfAttendanceInformation attendanceInformation;

	private Boolean identityChecked;

}
