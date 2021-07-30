package iris.client_bff.cases.eps;

import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.ContactInformation;
import iris.client_bff.cases.eps.dto.ContactPerson;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Event;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.eps.dto.WorkPlace;
import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.core.web.dto.Address;
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
	private static final String EXCEPTION_MESSAGE_FIRSTNAME = " - firstName";
	private static final String EXCEPTION_MESSAGE_LASTNAME = " - lastName";
	private static final String EXCEPTION_MESSAGE_EMAIL = " - email";
	private static final String EXCEPTION_MESSAGE_PHONE = " - phone";
	private static final String EXCEPTION_MESSAGE_MOBILPHONE = " - mobilePhone";
	private static final String EXCEPTION_MESSAGE_NAME = " - name";
	private static final String EXCEPTION_MESSAGE_STREET = " - street";
	private static final String EXCEPTION_MESSAGE_ZIP_CODE = " - zipCode";
	private static final String EXCEPTION_MESSAGE_HOUSE_NUMBER = " - houseNumber";
	private static final String EXCEPTION_MESSAGE_CITY = " - city";
	private static final String EXCEPTION_MESSAGE_ADDITIONAL_INFORMATION = " - additionalInformation";
	private static final String EXCEPTION_MESSAGE_POINT_OF_CONTACT = " - pointOfContact";
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
			log.trace("Start submission {}: events = {};", dataAuthorizationToken, events);

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

		if (events.getEvents() != null) {
			for (Event event : events.getEvents()) {

				if (ValidationHelper.isPossibleAttack(event.getName(), EXCEPTION_MESSAGE_NAME)) {
					event.setName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(event.getPhone(), EXCEPTION_MESSAGE_PHONE)) {
					event.setPhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(event.getAdditionalInformation(), EXCEPTION_MESSAGE_ADDITIONAL_INFORMATION)) {
					event.setAdditionalInformation(ErrorMessages.INVALID_INPUT_STRING);
				}

				event.setAddress(validateAddress(event.getAddress()));
			}
		}

		return true;
	}

	private boolean validateContacts(Contacts contacts) {

		if (contacts == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (contacts.getContactPersons() != null) {
			for (ContactPerson contactPerson : contacts.getContactPersons()) {

				if (ValidationHelper.isPossibleAttack(contactPerson.getLastName(), EXCEPTION_MESSAGE_LASTNAME)) {
					contactPerson.setLastName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(contactPerson.getFirstName(), EXCEPTION_MESSAGE_FIRSTNAME)) {
					contactPerson.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(contactPerson.getEmail(), EXCEPTION_MESSAGE_EMAIL)) {
					contactPerson.setEmail(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(contactPerson.getPhone(), EXCEPTION_MESSAGE_PHONE)) {
					contactPerson.setPhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(contactPerson.getMobilePhone(), EXCEPTION_MESSAGE_MOBILPHONE)) {
					contactPerson.setMobilePhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				contactPerson.setWorkPlace(validateWorkPlace(contactPerson.getWorkPlace()));

				contactPerson.setContactInformation(contactPerson.getContactInformation());
			}
		}

		if (contacts.getStartDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_START_DATE + EXCEPTION_MESSAGE_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (contacts.getEndDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_END_DATE + EXCEPTION_MESSAGE_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
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

			if (ValidationHelper.isPossibleAttack(address.getCity(), EXCEPTION_MESSAGE_CITY)) {
				address.setCity(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(address.getHouseNumber(), EXCEPTION_MESSAGE_HOUSE_NUMBER)) {
				address.setHouseNumber(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(address.getStreet(), EXCEPTION_MESSAGE_STREET)) {
				address.setStreet(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(address.getZipCode(), EXCEPTION_MESSAGE_ZIP_CODE)) {
				address.setZipCode(ErrorMessages.INVALID_INPUT_STRING);
			}

			return address;
		}

		return null;
	}

	private WorkPlace validateWorkPlace(WorkPlace workPlace) {
		if (workPlace != null) {

			if (ValidationHelper.isPossibleAttack(workPlace.getName(), EXCEPTION_MESSAGE_NAME)) {
				workPlace.setName(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(workPlace.getPhone(), EXCEPTION_MESSAGE_PHONE)) {
				workPlace.setPhone(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(workPlace.getPointOfContact(), EXCEPTION_MESSAGE_POINT_OF_CONTACT)) {
				workPlace.setPointOfContact(ErrorMessages.INVALID_INPUT_STRING);
			}

			workPlace.setAddress(validateAddress(workPlace.getAddress()));

			return workPlace;
		}
		return null;
	}

	private boolean validateCaseDataProvider(CaseDataProvider dataProvider) {

		if (dataProvider == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (ValidationHelper.isPossibleAttack(dataProvider.getFirstName(), EXCEPTION_MESSAGE_FIRSTNAME)) {
			dataProvider.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(dataProvider.getLastName(), EXCEPTION_MESSAGE_LASTNAME)) {
			dataProvider.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (dataProvider.getDateOfBirth() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_DATE_OF_BIRTH + EXCEPTION_MESSAGE_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return true;
	}
}
