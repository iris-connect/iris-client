package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The data request that will be sent by the FE.
 */
@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class DataRequestClient {

	@NotBlank
	private String locationId;

	@NotBlank
	private String providerId;

	private String name;

	@NotBlank
	private String externalRequestId;

	@NotNull
	private Instant start;

	@NotNull
	private Instant end;

	private String requestDetails;

	private String comment;

}
