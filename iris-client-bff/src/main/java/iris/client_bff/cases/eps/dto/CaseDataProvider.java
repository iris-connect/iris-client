
package iris.client_bff.cases.eps.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.serialization.DefuseJsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class CaseDataProvider {

	@DefuseJsonString(maxLength = 256)
	String firstName;

	@DefuseJsonString(maxLength = 256)
	String lastName;
}
