package iris.client_bff.events.message.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
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

	public static ImportSelectionDto toModel(String payload) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(payload, ImportSelectionDto.class);
		} catch (Exception e) {
			throw new IrisMessageDataException("iris_message.invalid_message_data");
		}
	}
}
