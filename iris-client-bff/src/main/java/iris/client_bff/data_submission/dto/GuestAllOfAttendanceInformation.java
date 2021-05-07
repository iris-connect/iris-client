package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Data
@Builder
@ToString
public class GuestAllOfAttendanceInformation {

	private String descriptionOfParticipation;
	private Instant attendFrom;
	private Instant attendTo;
	private String additionalInformation;
}
