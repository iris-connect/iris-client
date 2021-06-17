
package iris.client_bff.cases.eps.dto;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ContactInformation {
	String firstContactDate;
	String lastContactDate;
	ContactCategory contactCategory;
	String basicConditions;
}
