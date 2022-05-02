package iris.client_bff.vaccination_info.message.dto;

import lombok.Data;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class ImportSelectionDto {

	@Valid
	@NotEmpty
	Set<@NotBlank String> employees;

}
