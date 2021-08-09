package iris.client_bff.events.eps;

import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.web.dto.GuestList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDataControllerImpl implements EventDataController {

	private final EventDataSubmissionService dataSubmissionService;

	@Override
	public String submitGuestList(JsonRpcClientDto client, UUID dataAuthorizationToken, GuestList guestList) {
		log.trace("Start submission {}", dataAuthorizationToken);

		return dataSubmissionService.findRequestAndSaveGuestList(dataAuthorizationToken, guestList);
	}
}
