package iris.client_bff.vaccination_info.message.dto;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ExportSelectionDto {

	@NotBlank
	String report;

	@Valid
	@NotEmpty
	Set<@NotBlank String> employees;

}
