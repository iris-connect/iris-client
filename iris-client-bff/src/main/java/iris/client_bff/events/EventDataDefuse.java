package iris.client_bff.events;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.core.web.dto.Address;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.events.web.dto.GuestAttendanceInformation;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static iris.client_bff.ui.messages.ErrorMessages.INVALID_INPUT;
import static iris.client_bff.ui.messages.ErrorMessages.INVALID_INPUT_STRING;

@Service
@RequiredArgsConstructor
public class EventDataDefuse {
    private final ValidationHelper validHelper;

    public void defuseGuestList(GuestList guestList) {
        GuestListDataProvider dataProvider = guestList.getDataProvider();
        if (dataProvider == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_INPUT + ": data provider");
        }

        guestList.setAdditionalInformation(defuseInput(guestList.getAdditionalInformation(),
                "additionalInformation", true, 500));

        dataProvider.setName(defuseInput(dataProvider.getName(),
                "dataProvider.name", true, 256));

        Address providerAddress = dataProvider.getAddress();
        if (providerAddress != null) {
            providerAddress.setCity(defuseInput(providerAddress.getCity(),
                    "dataProvider.address.city", true, 256));
            providerAddress.setStreet(defuseInput(providerAddress.getStreet(),
                    "dataProvider.address.street", true, 256));
            providerAddress.setHouseNumber(defuseInput(providerAddress.getHouseNumber(),
                    "dataProvider.address.houseNumber", true, 256));
            providerAddress.setZipCode(defuseInput(providerAddress.getZipCode(),
                    "dataProvider.address.zipCode", true, 10));
        }

        if (guestList.getGuests() != null) {
            for (Guest guest : guestList.getGuests()) {
                guest.setFirstName(defuseInput(guest.getFirstName(), "guest.firstName", true, 100));
                guest.setLastName(defuseInput(guest.getLastName(), "guest.lastName", true, 100));
                guest.setEmail(defuseInput(guest.getEmail(), "guest.email", true, 100));
                guest.setPhone(defuseInputPhone(guest.getPhone(), "guest.phone", true, 100));
                guest.setMobilePhone(defuseInputPhone(guest.getMobilePhone(), "guest.mobilePhone", true, 100));

                Address guestAddress = guest.getAddress();
                if (guestAddress != null) {
                    guestAddress.setStreet(defuseInput(guestAddress.getStreet(), "guest.address.street", true, 256));
                    guestAddress
                            .setHouseNumber(defuseInput(guestAddress.getHouseNumber(), "guest.address.houseNumber", true, 256));
                    guestAddress.setZipCode(defuseInput(guestAddress.getZipCode(), "guest.address.zipCode", true, 10));
                    guestAddress.setCity(defuseInput(guestAddress.getCity(), "guest.address.city", true, 256));
                }

                GuestAttendanceInformation attendInfo = guest.getAttendanceInformation();
                attendInfo.setAdditionalInformation(defuseInput(attendInfo.getAdditionalInformation(),
                        "guest.attendanceInformation.additionalInformation", true, 500));
                attendInfo.setDescriptionOfParticipation(defuseInput(attendInfo.getDescriptionOfParticipation(),
                        "guest.attendanceInformation.descriptionOfParticipation", true, 500));
            }
        }
    }

    private String defuseInput(String input, String field, boolean obfuscateLogging, int maxLength) {
        if (validHelper.isPossibleAttack(input, field, obfuscateLogging)) {
            return INVALID_INPUT_STRING;
        }

        return StringUtils.truncate(input, maxLength);
    }

    private String defuseInputPhone(String input, String field, boolean obfuscateLogging, int maxLength) {
        if (validHelper.isPossibleAttackForPhone(input, field, obfuscateLogging)) {
            return INVALID_INPUT_STRING;
        }

        return StringUtils.truncate(input, maxLength);
    }
}
