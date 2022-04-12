package iris.client_bff.core.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.serialization.DefuseJsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class AddressWithDefuseData {

	@NotBlank
	@DefuseJsonString(maxLength = 256)
	private String street;

	@NotBlank
	@DefuseJsonString(maxLength = 256)
	private String houseNumber;

	@NotBlank
	@DefuseJsonString(maxLength = 10)
	private String zipCode;

	@NotBlank
	@DefuseJsonString(maxLength = 256)
	private String city;
}
