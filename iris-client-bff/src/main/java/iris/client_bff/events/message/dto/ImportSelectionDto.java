package iris.client_bff.events.message.dto;

import lombok.Data;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class ImportSelectionDto {

	@Valid
	@NotEmpty
	List<@NotBlank String> guests;

}
