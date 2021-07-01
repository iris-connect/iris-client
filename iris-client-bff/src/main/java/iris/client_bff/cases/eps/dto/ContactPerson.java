
package iris.client_bff.cases.eps.dto;

import static lombok.AccessLevel.PRIVATE;

import iris.client_bff.core.web.dto.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ContactPerson extends Person {
	WorkPlace workPlace;
	ContactInformation contactInformation;
}
