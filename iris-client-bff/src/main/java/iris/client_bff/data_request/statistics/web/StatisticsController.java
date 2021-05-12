package iris.client_bff.data_request.statistics.web;

import iris.client_bff.data_request.DataRequest.Status;
import iris.client_bff.data_request.cases.IndexCaseService;
import iris.client_bff.data_request.events.EventDataRequestService;
import iris.client_bff.data_request.statistics.web.dto.StatisticsDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/data-requests-client/statistics")
public class StatisticsController {

	private IndexCaseService indexCaseService;
	private EventDataRequestService eventService;

	@GetMapping
	@ResponseStatus(OK)
	public StatisticsDTO getWeeklyData() {
		StatisticsDTO stats = new StatisticsDTO();

		Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);

		stats.setIndexCasesCount(indexCaseService.getCountSinceDate(oneWeekAgo));
		stats.setEventsCount(eventService.getCountSinceDate(oneWeekAgo));
		stats.setSumStatus(indexCaseService.getCountWithStatus(Status.DATA_RECEIVED)
				+ eventService.getCountWithStatus(Status.DATA_RECEIVED));

		return stats;
	}
}
