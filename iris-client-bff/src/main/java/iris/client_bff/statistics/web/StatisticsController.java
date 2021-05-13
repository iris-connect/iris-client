package iris.client_bff.statistics.web;

import static org.springframework.http.HttpStatus.OK;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.IndexCaseService;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.statistics.web.dto.StatisticsDTO;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/data-requests-client/statistics")
public class StatisticsController {

	private IndexCaseService indexCaseService;
	private EventDataRequestService eventService;

	@GetMapping
	@ResponseStatus(OK)
	public StatisticsDTO getWeeklyData() {
		Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
		int indexCasesCount = indexCaseService.getCountSinceDate(oneWeekAgo);
		int eventsCount = eventService.getCountSinceDate(oneWeekAgo);
		int sumStatus = indexCaseService.getCountWithStatus(CaseDataRequest.Status.DATA_RECEIVED)
				+ eventService.getCountWithStatus(EventDataRequest.Status.DATA_RECEIVED);

		return StatisticsDTO.builder()
				.indexCasesCount(indexCasesCount)
				.eventsCount(eventsCount)
				.sumStatus(sumStatus)
				.build();
	}
}
