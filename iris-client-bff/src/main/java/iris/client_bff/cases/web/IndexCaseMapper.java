package iris.client_bff.cases.web;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
import iris.client_bff.cases.web.submission_dto.*;
import iris.client_bff.core.Sex;
import iris.client_bff.core.web.dto.Address;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class IndexCaseMapper {

    public static IndexCaseDetailsDTO mapDetailed(CaseDataRequest indexCase) {
        return IndexCaseDetailsDTO.builder()
                .caseId(indexCase.getId().toString())
                .comment(indexCase.getComment())
                .externalCaseId(indexCase.getRefId())
                .name(indexCase.getName())
                .status(IndexCaseStatusDTO.valueOf(indexCase.getStatus().name()))
                .start(indexCase.getRequestStart())
                .end(indexCase.getRequestEnd())
                .readableToken(indexCase.getReadableToken())
                .build();
    }

    public static IndexCaseDTO map(CaseDataRequest indexCase) {

        return IndexCaseDTO.builder()
                .caseId(indexCase.getId().toString())
                .comment(indexCase.getComment())
                .externalCaseId(indexCase.getRefId())
                .name(indexCase.getName())
                .status(IndexCaseStatusDTO.valueOf(indexCase.getStatus().name()))
                .start(indexCase.getRequestStart())
                .end(indexCase.getRequestEnd())
                .build();
    }

    public static ContactsAndEvents mapDataSubmission(CaseDataSubmission submission) {
        var contactsAndEventsDataProvider = ContactsAndEventsDataProvider.builder()
                .firstName(submission.getDataProvider().getFirstName())
                .lastName(submission.getDataProvider().getLastName())
                .dateOfBirth(submission.getDataProvider().getDateOfBirth())
                .build();

        var contacts = ContactPersonList.builder()
                .contactPersons(submission.getContacts().stream().map(contact -> {

                    var contactPerson = ContactPerson.builder()
                            .dateOfBirth(contact.getDateOfBirth())
                            .firstName(contact.getFirstName())
                            .lastName(contact.getLastName())
                            .phone(contact.getPhone())
                            .email(contact.getEmail())
                            .mobilePhone(contact.getMobilePhone())
                            .sex(Sex.valueOf(Optional.ofNullable(contact.getSex()).orElse(Sex.UNKNOWN).name()))
                            .build();

                    if (contact.getAddress() != null) {
                        var address = Address.builder()
                                .city(contact.getAddress().getCity())
                                .houseNumber(contact.getAddress().getHouseNumber())
                                .street(contact.getAddress().getStreet())
                                .zipCode(contact.getAddress().getZipCode())
                                .build();

                        contactPerson.setAddress(address);
                    }

                    var contactInformation = ContactPersonAllOfContactInformation
                            .builder()
                            .basicConditions(contact.getBasicConditions())
                            .firstContactDate(contact.getFirstContactDate())
                            .lastContactDate(contact.getLastContactDate())
                            .build();

                    if (contact.getContactCategory() != null) {
                        contactInformation.setContactCategory(ContactCategory.valueOf(contact.getContactCategory().name()));
                    }

                    contactPerson.setContactInformation(contactInformation);

                    var workplace = ContactPersonAllOfWorkPlace.builder()
                            .name(contact.getWorkplaceName())
                            .pointOfContact(contact.getWorkplacePointOfContact())
                            .phone(contact.getWorkplacePhone())
                            .build();

                    if (contact.getWorkplaceAddress() != null) {
                        workplace.setAddress(Address.builder()
                                .zipCode(contact.getWorkplaceAddress().getZipCode())
                                .city(contact.getWorkplaceAddress().getCity())
                                .houseNumber(contact.getWorkplaceAddress().getHouseNumber())
                                .street(contact.getWorkplaceAddress().getStreet())
                                .build());
                    }

                    contactPerson.setWorkPlace(workplace);

                    return contactPerson;
                }).collect(Collectors.toList()))
                .startDate(submission.getContactsStartDateAsLocalDate())
                .endDate(submission.getContactsEndDateAsLocalDate())
                .build();

        EventList events;
        if (submission.getEvents().isEmpty()) {
            events = EventList.builder().build();
        } else {
            events = EventList.builder()
                    .events(submission.getEvents().stream().map(event -> {

                        var eventResult = Event.builder()
                                .name(event.getName())
                                .phone(event.getPhone())
                                .additionalInformation(event.getAdditionalInformation())
                                .build();

                        iris.client_bff.core.model.Address addressDB = event.getAddress();
                        if (addressDB != null) {
                            var address = Address.builder()
                                    .city(addressDB.getCity())
                                    .houseNumber(addressDB.getHouseNumber())
                                    .street(addressDB.getStreet())
                                    .zipCode(addressDB.getZipCode())
                                    .build();
                            eventResult.setAddress(address);
                        }

                        return eventResult;
                    }).collect(Collectors.toList()))
                    .endDate(submission.getEventsEndDateAsLocalDate())
                    .startDate(submission.getEventsStartDateAsLocalDate())
                    .build();
        }

        return ContactsAndEvents.builder()
                .dataProvider(contactsAndEventsDataProvider)
                .contacts(contacts)
                .events(events)
                .build();
    }
}
