package iris.client_bff.events.eps;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.core.web.dto.Address;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.events.web.dto.GuestAttendanceInformation;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static iris.client_bff.ui.messages.ErrorMessages.INVALID_INPUT;
import static iris.client_bff.ui.messages.ErrorMessages.INVALID_INPUT_STRING;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDataControllerImpl implements EventDataController {

	private final EventDataSubmissionService dataSubmissionService;
	private final ValidationHelper validHelper;

	@Override
	public String submitGuestList(JsonRpcClientDto client, UUID dataAuthorizationToken, GuestList guestList) {
		log.trace("Start submission {}", dataAuthorizationToken);

		if(dataAuthorizationToken != null && ValidationHelper.isUUIDInputValid(dataAuthorizationToken.toString(), "dataAuthorizationToken")) {
			validateGuestList(guestList);

			return dataSubmissionService.findRequestAndSaveGuestList(dataAuthorizationToken, guestList);
		}
		// token invalid
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT+": data auth token");
	}

	private void validateGuestList(GuestList guestList) {
		GuestListDataProvider dataProvider = guestList.getDataProvider();
		if(dataProvider == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_INPUT+": data provider");
		}

		guestList.setAdditionalInformation(validateInput(guestList.getAdditionalInformation(),
				"additionalInformation", false, 500));

		dataProvider.setName(validateInput(dataProvider.getName(),
				"dataProvider.name", false, 256));

		Address providerAddress = dataProvider.getAddress();
		if(providerAddress != null){
			providerAddress.setCity(validateInput(providerAddress.getCity(),
					"dataProvider.address.city", false, 256));
			providerAddress.setStreet(validateInput(providerAddress.getStreet(),
					"dataProvider.address.street", false, 256));
			providerAddress.setHouseNumber(validateInput(providerAddress.getHouseNumber(),
					"dataProvider.address.houseNumber", false, 256));
			providerAddress.setZipCode(validateInput(providerAddress.getZipCode(),
					"dataProvider.address.zipCode", false, 10));
		}

		if(guestList.getGuests() != null) {
			for (Guest guest : guestList.getGuests()) {
				guest.setFirstName(validateInput(guest.getFirstName(),"guest.firstName", true, 100));
				guest.setLastName(validateInput(guest.getLastName(),"guest.lastName", true, 100));
				guest.setEmail(validateInput(guest.getEmail(),"guest.email", true, 100));
				guest.setPhone(validateInput(guest.getPhone(),"guest.phone", true, 100));
				guest.setMobilePhone(validateInput(guest.getMobilePhone(),"guest.mobilePhone", true, 100));

				Address guestAddress = guest.getAddress();
				if(guestAddress != null) {
					guestAddress.setStreet(validateInput(guestAddress.getStreet(), "guest.address.street", true, 256));
					guestAddress.setHouseNumber(validateInput(guestAddress.getHouseNumber(), "guest.address.houseNumber", true, 256));
					guestAddress.setZipCode(validateInput(guestAddress.getZipCode(), "guest.address.zipCode", false, 10));
					guestAddress.setCity(validateInput(guestAddress.getCity(), "guest.address.city", false, 256));
				}

				GuestAttendanceInformation attendInfo = guest.getAttendanceInformation();
				attendInfo.setAdditionalInformation(validateInput(attendInfo.getAdditionalInformation(),
						"guest.attendanceInformation.additionalInformation", false, 500));
				attendInfo.setDescriptionOfParticipation(validateInput(attendInfo.getDescriptionOfParticipation(),
						"guest.attendanceInformation.descriptionOfPartizipation", false, 500));
			}
		}
	}

	private String validateInput(String input, String field, boolean obfuscateLogging, int maxLength) {
		if(validHelper.isPossibleAttack(input, field, obfuscateLogging)) {
			return INVALID_INPUT_STRING;
		}

		if(input != null && input.length() > maxLength) {
			return input.substring(0, maxLength);
		}

		return input;
	}
}
