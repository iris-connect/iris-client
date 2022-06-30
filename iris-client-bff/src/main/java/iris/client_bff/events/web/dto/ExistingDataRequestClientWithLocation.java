package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ExistingDataRequestClientWithLocation {
	public enum StatusEnum {
		DATA_REQUESTED, DATA_RECEIVED, CLOSED, ABORTED;
	}

	private StatusEnum status;

	private String code;

	private String name;

	private String externalRequestId;

	private Instant start;

	private Instant end;

	private Instant requestedAt;

	private Instant lastUpdatedAt;

	private String requestDetails;

	private LocationInformation locationInformation;
}
