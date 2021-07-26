package iris.client_bff.events.eps;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.EventEmailProvider;
import iris.client_bff.events.web.dto.GuestList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDataControllerImpl implements EventDataController {

	private final EventDataRequestService requestService;
	private final EventDataSubmissionService dataSubmissionService;
	private final EventEmailProvider eventEmailProvider;

	@Override
	public String submitGuestList(JsonRpcClientDto client, UUID dataAuthorizationToken, GuestList guestList) {
		log.trace("Start submission {}", dataAuthorizationToken);

		// Todo check client.getName() vs. providerId

		return requestService.findById(dataAuthorizationToken).map(dataRequest -> {

			if (dataRequest.getStatus().equals(EventDataRequest.Status.ABORTED)) {
				log.trace("Submission {} for aborted event {}", dataAuthorizationToken, dataRequest.getId());
				return "Error: Submission not allowed for " + dataAuthorizationToken.toString() + ". Request was aborted.";
			}

			dataSubmissionService.save(dataRequest, guestList);

			eventEmailProvider.sendDataReceivedEmailAsynchronously(dataRequest);

			log.trace("Done submission {}", dataAuthorizationToken);

			return "OK";

		}).orElseGet(() -> {

			// TODO sufficient?
			// probably throw exception

			log.error("Data submission for unknown data request occurred: {}", dataAuthorizationToken);

			return "Unknown dataAuthorizationToken: " + dataAuthorizationToken.toString();
		});
	}
}
