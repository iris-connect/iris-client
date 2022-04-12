
package iris.client_bff.cases.eps.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.serialization.DefuseJsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ContactInformation {

	Instant firstContactDate;
	Instant lastContactDate;
	ContactCategory contactCategory;

	@DefuseJsonString(maxLength = 256)
	String basicConditions;
}
