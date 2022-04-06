package iris.client_bff.cases.web.request_dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.validation.NoSignOfAttack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class IndexCaseInsertDTO {
	
	@NoSignOfAttack
	private String comment;

	@NoSignOfAttack
	private String externalCaseId;

	@NoSignOfAttack
	private String name;

	@NotNull
	private Instant start;

	private Instant end;
}
