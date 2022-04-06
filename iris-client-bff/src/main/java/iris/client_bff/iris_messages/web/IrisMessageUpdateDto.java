package iris.client_bff.iris_messages.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
class IrisMessageUpdateDto {

	@NotNull
	private Boolean isRead;

}
