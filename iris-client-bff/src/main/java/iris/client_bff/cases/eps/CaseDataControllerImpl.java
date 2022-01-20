package iris.client_bff.cases.eps;

import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.ContactInformation;
import iris.client_bff.cases.eps.dto.ContactPerson;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Event;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.eps.dto.WorkPlace;
import iris.client_bff.config.SuspiciouslyCaseRequestProperties;
import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.core.web.dto.Address;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

	private static final String FIELD_DATA_AUTHORIZATION_TOKEN = "dataAuthorizationToken";
	private static final String FIELD_FIRSTNAME = "firstName";
	private static final String FIELD_LASTNAME = "lastName";
	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_PHONE = "phone";
	private static final String FIELD_MOBILE_PHONE = "mobilePhone";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_STREET = "street";
	private static final String FIELD_ZIP_CODE = "zipCode";
	private static final String FIELD_HOUSE_NUMBER = "houseNumber";
	private static final String FIELD_CITY = "city";
	private static final String FIELD_ADDITIONAL_INFORMATION = "additionalInformation";
	private static final String FIELD_POINT_OF_CONTACT = "pointOfContact";

	private final ValidationHelper validHelper;
	private final CaseDataSubmissionService submissionService;
	private final SuspiciouslyCaseRequestProperties suspiciouslyRequest;

	@Override
	public String submitContactAndEventData(
			String dataAuthorizationToken,
			@Valid Contacts contacts,
			@Valid Events events,
			@Valid CaseDataProvider dataProvider) {

		validatePostLimit(contacts.getContactPersons(), "proxy", "contacts");
		validatePostLimit(events.getEvents(), "proxy", "events");

		if (validateEvents(events)
				&& validateContacts(contacts)
				&& validateCaseDataProvider(dataProvider)) {
			log.trace("Start submission {}", dataAuthorizationToken);

			return submissionService.validateAndSaveData(dataAuthorizationToken, contacts, events, dataProvider);

		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}
	}

	private void validatePostLimit(List<?> list, String client, String type) {

		if (validHelper.isPostOutOfLimit(list, client,
				suspiciouslyRequest.getDataBlockingThreshold(), suspiciouslyRequest.getDataWarningThreshold(),
				type)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request content exceeded blocking limit!");
		}
	}

	private boolean validateEvents(Events events) {

		if (events == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (events.getEvents() != null) {
			for (Event event : events.getEvents()) {

				if (validHelper.isPossibleAttack(event.getName(), FIELD_NAME, true)) {
					event.setName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (validHelper.isPossibleAttackForPhone(event.getPhone(), FIELD_PHONE, true)) {
					event.setPhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (validHelper.isPossibleAttack(event.getAdditionalInformation(), FIELD_ADDITIONAL_INFORMATION, true)) {
					event.setAdditionalInformation(ErrorMessages.INVALID_INPUT_STRING);
				}

				event.setAddress(validateAddress(event.getAddress()));
			}
		}

		return true;
	}

	private boolean validateContacts(Contacts contacts) {

		if (contacts == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (contacts.getContactPersons() != null) {
			for (ContactPerson contactPerson : contacts.getContactPersons()) {

				if (validHelper.isPossibleAttack(contactPerson.getLastName(), FIELD_LASTNAME, true)) {
					contactPerson.setLastName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (validHelper.isPossibleAttack(contactPerson.getFirstName(), FIELD_FIRSTNAME, true)) {
					contactPerson.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (validHelper.isPossibleAttack(contactPerson.getEmail(), FIELD_EMAIL, true)) {
					contactPerson.setEmail(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (validHelper.isPossibleAttackForPhone(contactPerson.getPhone(), FIELD_PHONE, true)) {
					contactPerson.setPhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (validHelper.isPossibleAttackForPhone(contactPerson.getMobilePhone(), FIELD_MOBILE_PHONE, true)) {
					contactPerson.setMobilePhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				contactPerson.setWorkPlace(validateWorkPlace(contactPerson.getWorkPlace()));

				contactPerson.setContactInformation(contactPerson.getContactInformation());
			}
		}

		return true;
	}

	private ContactInformation validateContactInformation(ContactInformation contactInformation) {

		if (contactInformation != null) {

			// TODO validate ContactInformation

			return contactInformation;
		}

		return null;
	}

	private Address validateAddress(Address address) {
		if (address != null) {

			if (validHelper.isPossibleAttack(address.getCity(), FIELD_CITY, true)) {
				address.setCity(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (validHelper.isPossibleAttack(address.getHouseNumber(), FIELD_HOUSE_NUMBER, true)) {
				address.setHouseNumber(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (validHelper.isPossibleAttack(address.getStreet(), FIELD_STREET, true)) {
				address.setStreet(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (validHelper.isPossibleAttack(address.getZipCode(), FIELD_ZIP_CODE, true)) {
				address.setZipCode(ErrorMessages.INVALID_INPUT_STRING);
			}

			return address;
		}

		return null;
	}

	private WorkPlace validateWorkPlace(WorkPlace workPlace) {
		if (workPlace != null) {

			if (validHelper.isPossibleAttack(workPlace.getName(), FIELD_NAME, true)) {
				workPlace.setName(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (validHelper.isPossibleAttackForPhone(workPlace.getPhone(), FIELD_PHONE, true)) {
				workPlace.setPhone(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (validHelper.isPossibleAttack(workPlace.getPointOfContact(), FIELD_POINT_OF_CONTACT, true)) {
				workPlace.setPointOfContact(ErrorMessages.INVALID_INPUT_STRING);
			}

			workPlace.setAddress(validateAddress(workPlace.getAddress()));

			return workPlace;
		}
		return null;
	}

	private boolean validateCaseDataProvider(CaseDataProvider dataProvider) {

		if (dataProvider == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (validHelper.isPossibleAttack(dataProvider.getFirstName(), FIELD_FIRSTNAME, true)) {
			dataProvider.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validHelper.isPossibleAttack(dataProvider.getLastName(), FIELD_LASTNAME, true)) {
			dataProvider.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		return true;
	}
}
