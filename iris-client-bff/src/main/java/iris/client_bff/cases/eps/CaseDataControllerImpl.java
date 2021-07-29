package iris.client_bff.cases.eps;

import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.core.utils.ValidationHelper;
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

	private static final String EXCEPTION_MESSAGE_NULL = "null";
	private static final String EXCEPTION_MESSAGE_START_DATE = " - startDate: ";
	private static final String EXCEPTION_MESSAGE_END_DATE = " - endDate: ";
	private static final String EXCEPTION_MESSAGE_DATE_OF_BIRTH = " - dateOfBirth: ";
	private static final String EXCEPTION_MESSAGE_DATA_AUTHORIZATION_TOKEN = " - dataAuthorizationToken: ";
	private static final String EXCEPTION_MESSAGE_FIRSTNAME = " - firstName: ";
	private static final String EXCEPTION_MESSAGE_LASTNAME = " - lastName: ";
	private final CaseDataSubmissionService submissionService;

	@Override
	public String submitContactAndEventData(
		UUID dataAuthorizationToken,
		@Valid Contacts contacts,
		@Valid Events events,
		@Valid CaseDataProvider dataProvider) {

		if (ValidationHelper
			.isUUIDInputValid(dataAuthorizationToken.toString(), EXCEPTION_MESSAGE_DATA_AUTHORIZATION_TOKEN + dataAuthorizationToken.toString())
			&& validateEvents(events)
			&& validateContacts(contacts)
			&& validateCaseDataProvider(dataProvider)) {
			log.trace("Start submission {}: contacts = {}; events = {}; dataProvider = {}", dataAuthorizationToken, contacts, events, dataProvider);

			return submissionService.validateAndSaveData(dataAuthorizationToken, contacts, events, dataProvider);

		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	private boolean validateEvents(Events events) {

		if (events == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (events.getStartDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_START_DATE + EXCEPTION_MESSAGE_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (events.getEndDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_END_DATE + EXCEPTION_MESSAGE_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		// TODO: validate Event List - requires validating Address: already done - move into model ?

		return true;
	}

	private boolean validateContacts(Contacts contacts) {

		if (contacts == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (contacts.getStartDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_START_DATE + EXCEPTION_MESSAGE_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (contacts.getEndDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_END_DATE + EXCEPTION_MESSAGE_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		// TODO: validate Contact Person List

		return true;
	}

	private boolean validateCaseDataProvider(CaseDataProvider dataProvider) {

		if (dataProvider == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (ValidationHelper.isPossibleAttack(dataProvider.getFirstName(), EXCEPTION_MESSAGE_FIRSTNAME + dataProvider.getFirstName())) {
			dataProvider.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(dataProvider.getLastName(), EXCEPTION_MESSAGE_LASTNAME + dataProvider.getLastName())) {
			dataProvider.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (dataProvider.getDateOfBirth() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_DATE_OF_BIRTH + EXCEPTION_MESSAGE_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return true;
	}
}
