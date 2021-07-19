package iris.client_bff.cases.eps;

import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.core.security.InputValidationUtility;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class CaseDataControllerImpl implements CaseDataController {

	private final InputValidationUtility inputValidationUtility;

	private final CaseDataSubmissionService submissionService;

	@Override
	public String submitContactAndEventData(
		UUID dataAuthorizationToken,
		@Valid Contacts contacts,
		@Valid Events events,
		@Valid CaseDataProvider dataProvider) {

		if (inputValidationUtility.isUUIDInputValid(dataAuthorizationToken.toString())
			&& isEventsInputValid(events)
			&& isContactsInputValid(contacts)
			&& isCaseDataProviderInputValid(dataProvider)) {
			log.trace("Start submission {}: contacts = {}; events = {}; dataProvider = {}", dataAuthorizationToken, contacts, events, dataProvider);

			return submissionService.validateAndSaveData(dataAuthorizationToken, contacts, events, dataProvider);

		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	private boolean isEventsInputValid(Events events) {
		if (events == null) {
			return false;
		}

		return true;
	}

	private boolean isContactsInputValid(Contacts contacts) {
		if (contacts == null) {
			return false;
		}

		return true;
	}

	private boolean isCaseDataProviderInputValid(CaseDataProvider dataProvider) {
		if (dataProvider == null) {
			return false;
		}

		return true;
	}
}
