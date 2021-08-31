
package iris.client_bff.cases.eps.dto;

import static lombok.AccessLevel.PRIVATE;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Contacts {
	List<ContactPerson> contactPersons = null;
	Instant startDate;
	Instant endDate;
}
