package iris.client_bff.events.eps;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class JsonRpcClientDto {

	private @NotNull String name;

}
