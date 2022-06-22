
package iris.client_bff.cases.eps.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.api.dto.AddressWithDefuseData;
import iris.client_bff.core.serialization.DefuseJsonString;
import iris.client_bff.core.validation.AttackDetector.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Event {

	@DefuseJsonString(maxLength = 256)
	String name;

	@DefuseJsonString(maxLength = 100, payload = Phone.class)
	String phone;

	AddressWithDefuseData address;

	@DefuseJsonString(maxLength = 256)
	String additionalInformation;
}
