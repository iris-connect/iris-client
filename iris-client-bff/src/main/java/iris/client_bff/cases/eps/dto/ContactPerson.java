
package iris.client_bff.cases.eps.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.web.dto.PersonWithDefuseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ContactPerson extends PersonWithDefuseData {

	WorkPlace workPlace;
	ContactInformation contactInformation;
}
