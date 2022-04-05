package iris.client_bff.events.message.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ImportSelectionDto {

	@Valid
	@NotEmpty
	List<@NotBlank String> guests;

}
