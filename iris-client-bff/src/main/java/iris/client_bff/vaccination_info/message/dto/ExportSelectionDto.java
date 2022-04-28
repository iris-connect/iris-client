package iris.client_bff.vaccination_info.message.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ExportSelectionDto {

	@NotNull
	UUID report;

	@Valid
	@NotEmpty
	Set<@NotBlank String> employees;

}
