package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.api.dto.AddressWithDefuseData;
import iris.client_bff.core.serialization.DefuseJsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class GuestListDataProvider {

	@DefuseJsonString(maxLength = 256)
	private String name;

	@NotNull
	@Valid
	private AddressWithDefuseData address;
}
