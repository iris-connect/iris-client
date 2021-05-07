package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ContactPersonAllOfWorkPlace {

	private String name;
	private String pointOfContact;
	private String phone;
	private Address address;
}
