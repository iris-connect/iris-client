package iris.client_bff.status.web;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequestService;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.statistics.web.dto.StatisticsDTO;
import iris.client_bff.status.StatusService;
import iris.client_bff.status.eps.dto.Ping;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/status")
public class StatusController {

	private StatusService statusService;

	@GetMapping
	@ResponseStatus(OK)
	public String getStatus() {

		return statusService.getStati();
	}

	@GetMapping("{epsEndpoint}")
	public Ping getEndpointStatus(@PathVariable String epsEndpoint) {
		return statusService.getEPSStatus(epsEndpoint);
	}

}
