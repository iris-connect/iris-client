package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.validation.NoSignOfAttack;
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
	@NoSignOfAttack
	private String locationId;

	@NotBlank
	@NoSignOfAttack
	private String providerId;

	@NoSignOfAttack
	private String name;

	@NotBlank
	@NoSignOfAttack
	private String externalRequestId;

	@NotNull
	private Instant start;

	@NotNull
	private Instant end;

	@NoSignOfAttack
	private String requestDetails;

	@NoSignOfAttack
	private String comment;
}
