package iris.client_bff.statistics.web;

import static org.springframework.http.HttpStatus.*;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequestService;
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

	private CaseDataRequestService caseDataRequestService;
	// private EventDataRequestService eventService;

	@GetMapping
	@ResponseStatus(OK)
	public StatisticsDTO getWeeklyData() {
		Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
		int indexCasesCount = caseDataRequestService.getCountSinceDate(oneWeekAgo);
		int eventsCount = 0;// eventService.getCountSinceDate(oneWeekAgo);
		int sumStatus = caseDataRequestService.getCountWithStatus(CaseDataRequest.Status.DATA_RECEIVED)
				+ 0;// eventService.getCountWithStatus(EventDataRequest.Status.DATA_RECEIVED);

		return StatisticsDTO.builder()
				.indexCasesCount(indexCasesCount)
				.eventsCount(eventsCount)
				.sumStatus(sumStatus)
				.build();
	}
}
