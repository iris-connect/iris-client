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

	private static final String ERR_MSG_NULL = "null";
	private static final String ERR_MSG_START_DATE = " - startDate: ";
	private static final String ERR_MSG_END_DATE = " - endDate: ";
	private static final String ERR_MSG_DATE_OF_BIRTH = " - dateOfBirth: ";
	private static final String ERR_MSG_DATA_AUTHORIZATION_TOKEN = " - dataAuthorizationToken: ";
	private static final String ERR_MSG_FIRSTNAME = " - firstName";
	private static final String ERR_MSG_LASTNAME = " - lastName";
	private static final String ERR_MSG_EMAIL = " - email";
	private static final String ERR_MSG_PHONE = " - phone";
	private static final String ERR_MSG_MOBILPHONE = " - mobilePhone";
	private static final String ERR_MSG_NAME = " - name";
	private static final String ERR_MSG_STREET = " - street";
	private static final String ERR_MSG_ZIP_CODE = " - zipCode";
	private static final String ERR_MSG_HOUSE_NUMBER = " - houseNumber";
	private static final String ERR_MSG_CITY = " - city";
	private static final String ERR_MSG_ADDITIONAL_INFORMATION = " - additionalInformation";
	private static final String ERR_MSG_POINT_OF_CONTACT = " - pointOfContact";
	private final CaseDataSubmissionService submissionService;

	@Override
	public String submitContactAndEventData(
		UUID dataAuthorizationToken,
		@Valid Contacts contacts,
		@Valid Events events,
		@Valid CaseDataProvider dataProvider) {

		if (ValidationHelper
			.isUUIDInputValid(dataAuthorizationToken.toString(), ERR_MSG_DATA_AUTHORIZATION_TOKEN)
			&& validateEvents(events)
			&& validateContacts(contacts)
			&& validateCaseDataProvider(dataProvider)) {
			log.trace("Start submission {}", dataAuthorizationToken);

			return submissionService.validateAndSaveData(dataAuthorizationToken, contacts, events, dataProvider);

		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}
	}

	private boolean validateEvents(Events events) {

		if (events == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (events.getStartDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT + ERR_MSG_START_DATE + ERR_MSG_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (events.getEndDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT + ERR_MSG_END_DATE + ERR_MSG_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (events.getEvents() != null) {
			for (Event event : events.getEvents()) {

				if (ValidationHelper.isPossibleAttack(event.getName(), ERR_MSG_NAME)) {
					event.setName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(event.getPhone(), ERR_MSG_PHONE)) {
					event.setPhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(event.getAdditionalInformation(), ERR_MSG_ADDITIONAL_INFORMATION)) {
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

				if (ValidationHelper.isPossibleAttack(contactPerson.getLastName(), ERR_MSG_LASTNAME)) {
					contactPerson.setLastName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(contactPerson.getFirstName(), ERR_MSG_FIRSTNAME)) {
					contactPerson.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(contactPerson.getEmail(), ERR_MSG_EMAIL)) {
					contactPerson.setEmail(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(contactPerson.getPhone(), ERR_MSG_PHONE)) {
					contactPerson.setPhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				if (ValidationHelper.isPossibleAttack(contactPerson.getMobilePhone(), ERR_MSG_MOBILPHONE)) {
					contactPerson.setMobilePhone(ErrorMessages.INVALID_INPUT_STRING);
				}

				contactPerson.setWorkPlace(validateWorkPlace(contactPerson.getWorkPlace()));

				contactPerson.setContactInformation(contactPerson.getContactInformation());
			}
		}

		if (contacts.getStartDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT + ERR_MSG_START_DATE + ERR_MSG_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (contacts.getEndDate() == null) {
			log.warn(ErrorMessages.INVALID_INPUT + ERR_MSG_END_DATE + ERR_MSG_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
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

			if (ValidationHelper.isPossibleAttack(address.getCity(), ERR_MSG_CITY)) {
				address.setCity(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(address.getHouseNumber(), ERR_MSG_HOUSE_NUMBER)) {
				address.setHouseNumber(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(address.getStreet(), ERR_MSG_STREET)) {
				address.setStreet(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(address.getZipCode(), ERR_MSG_ZIP_CODE)) {
				address.setZipCode(ErrorMessages.INVALID_INPUT_STRING);
			}

			return address;
		}

		return null;
	}

	private WorkPlace validateWorkPlace(WorkPlace workPlace) {
		if (workPlace != null) {

			if (ValidationHelper.isPossibleAttack(workPlace.getName(), ERR_MSG_NAME)) {
				workPlace.setName(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(workPlace.getPhone(), ERR_MSG_PHONE)) {
				workPlace.setPhone(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (ValidationHelper.isPossibleAttack(workPlace.getPointOfContact(), ERR_MSG_POINT_OF_CONTACT)) {
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

		if (ValidationHelper.isPossibleAttack(dataProvider.getFirstName(), ERR_MSG_FIRSTNAME)) {
			dataProvider.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(dataProvider.getLastName(), ERR_MSG_LASTNAME)) {
			dataProvider.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (dataProvider.getDateOfBirth() == null) {
			log.warn(ErrorMessages.INVALID_INPUT + ERR_MSG_DATE_OF_BIRTH + ERR_MSG_NULL);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		return true;
	}
}
