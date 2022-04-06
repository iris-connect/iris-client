
package iris.client_bff.cases.eps.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ContactInformation {
	Instant firstContactDate;
	Instant lastContactDate;
	ContactCategory contactCategory;
	String basicConditions;
}
