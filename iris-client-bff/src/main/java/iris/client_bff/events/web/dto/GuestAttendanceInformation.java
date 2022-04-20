package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.serialization.DefuseJsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class GuestAttendanceInformation {

	@Size(max = 500)
	@DefuseJsonString(maxLength = 500)
	private String descriptionOfParticipation;

	private Instant attendFrom;
	private Instant attendTo;

	@Size(max = 500)
	@DefuseJsonString(maxLength = 500)
	private String additionalInformation;
}
