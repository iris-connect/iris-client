package iris.client_bff.events.eps;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class JsonRpcClientDto {

	@JsonProperty("Name")
	private @NotNull String name;

}
