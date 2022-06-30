package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.validation.NoSignOfAttack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class EventUpdateDTO {

	@NoSignOfAttack
	private String comment;

	@NoSignOfAttack
	private String externalRequestId;

	@NoSignOfAttack
	private String name;

	private EventStatusDTO status;
}
