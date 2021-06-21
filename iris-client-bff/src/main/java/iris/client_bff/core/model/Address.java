package iris.client_bff.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.persistence.Id;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Address {

	@Id
	private UUID address_Id = UUID.randomUUID();

	private String street;
	private String houseNumber;
	private String zipCode;
	private String city;
}
