package iris.client_bff.events.eps;

import static org.apache.commons.lang3.RegExUtils.replaceAll;
import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.config.SuspiciouslyEventRequestProperties;
import iris.client_bff.core.validation.AttackDetector;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDataControllerImpl implements EventDataController {

	private static final Pattern NUMBER_REGEX = Pattern.compile("^\\d+$");
	private static final Pattern NUMBER_REPLACE_REGEX = Pattern.compile("[\\s\\-_+#*.,:;()/|]");

	private final EventDataSubmissionService dataSubmissionService;
	private final AttackDetector attackDetector;
	private final SuspiciouslyEventRequestProperties suspiciouslyRequest;

	@Override
	public String submitGuestList(JsonRpcClientDto client, UUID dataAuthorizationToken, GuestList guestList) {

		log.trace("Start submission {}", dataAuthorizationToken);

		if (attackDetector.isPostOutOfLimit(guestList.getGuests(), client.getName(),
				suspiciouslyRequest.getDataBlockingThreshold(), suspiciouslyRequest.getDataWarningThreshold(),
				"guests")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request content exceeded blocking limit!");
		}

		if (dataAuthorizationToken == null) {
			// token invalid
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT + ": data auth token");
		}

		fixInvalidPhoneNumbersInGuestList(guestList);

		return dataSubmissionService.findRequestAndSaveGuestList(dataAuthorizationToken, client.getName(), guestList);
	}

	// use mobile number as phone number if mobile number is valid and phone number is not
	private void fixInvalidPhoneNumbersInGuestList(GuestList guestList) {

		if (guestList.getGuests() != null) {

			guestList.getGuests().stream()
					.filter(it -> !isPossiblePhoneNumber(it.getPhone()) && isPossiblePhoneNumber(it.getMobilePhone()))
					.forEach(guest -> guest.setPhone(guest.getMobilePhone()));
		}
	}

	private boolean isPossiblePhoneNumber(String phoneNumber) {

		if (isBlank(phoneNumber)) {
			return false;
		}

		var number = replaceAll(phoneNumber, NUMBER_REPLACE_REGEX, "");

		return NUMBER_REGEX.matcher(number).matches();
	}
}
