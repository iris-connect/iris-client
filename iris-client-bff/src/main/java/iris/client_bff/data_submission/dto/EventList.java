package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@ToString
public class EventList {

	@Builder.Default
	private List<Event> events = new ArrayList<>();
	private LocalDate startDate;
	private LocalDate endDate;
}
