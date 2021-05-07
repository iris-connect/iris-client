package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Address {

	private String street;
	private String houseNumber;
	private String zipCode;
	private String city;
}
