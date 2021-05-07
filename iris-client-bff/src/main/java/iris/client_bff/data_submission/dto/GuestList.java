package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@ToString
public class GuestList {

	@Builder.Default
	private List<Guest> guests = new ArrayList<>();
	private GuestListDataProvider dataProvider;
	private String additionalInformation;
	private Instant startDate;
	private Instant endDate;
}
