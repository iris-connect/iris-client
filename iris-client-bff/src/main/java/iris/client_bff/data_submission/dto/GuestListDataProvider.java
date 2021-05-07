package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class GuestListDataProvider {

	private String name;
	private Address address;
}
