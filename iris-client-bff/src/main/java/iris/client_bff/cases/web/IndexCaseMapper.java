package iris.client_bff.cases.web;

import static lombok.AccessLevel.PRIVATE;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
<<<<<<< Updated upstream
import iris.client_bff.cases.web.submission_dto.*;
=======
import iris.client_bff.cases.web.submission_dto.ContactCategory;
import iris.client_bff.cases.web.submission_dto.ContactPerson;
import iris.client_bff.cases.web.submission_dto.ContactPersonAllOfContactInformation;
import iris.client_bff.cases.web.submission_dto.ContactPersonAllOfWorkPlace;
import iris.client_bff.cases.web.submission_dto.ContactPersonList;
import iris.client_bff.cases.web.submission_dto.ContactsAndEvents;
import iris.client_bff.cases.web.submission_dto.ContactsAndEventsDataProvider;
import iris.client_bff.cases.web.submission_dto.Event;
import iris.client_bff.cases.web.submission_dto.EventList;
>>>>>>> Stashed changes
import iris.client_bff.core.Sex;
import iris.client_bff.core.web.dto.Address;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

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
				.submissionUri(indexCase.getDwSubmissionUri())
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

					var address = Address.builder()
							.city(contact.getAddress().getCity())
							.houseNumber(contact.getAddress().getHouseNumber())
							.street(contact.getAddress().getStreet())
							.zipCode(contact.getAddress().getZipCode())
							.build();

					var contactInformation = ContactPersonAllOfContactInformation
							.builder()
							.contactCategory(ContactCategory.valueOf(contact.getContactCategory().name()))
							.basicConditions(contact.getBasicConditions())
							.firstContactDate(contact.getFirstContactDate())
							.lastContactDate(contact.getLastContactDate())
							.build();

					var workplace = ContactPersonAllOfWorkPlace.builder()
							.name(contact.getWorkplaceName())
							.pointOfContact(contact.getWorkplacePointOfContact())
							.phone(contact.getWorkplacePhone())
							.address(Address.builder()
									.zipCode(contact.getWorkplaceAddress().getZipCode())
									.city(contact.getWorkplaceAddress().getCity())
									.houseNumber(contact.getWorkplaceAddress().getHouseNumber())
									.street(contact.getWorkplaceAddress().getStreet())
									.build())
							.build();

					return ContactPerson.builder()
							.dateOfBirth(contact.getDateOfBirth())
							.firstName(contact.getFirstName())
							.lastName(contact.getLastName())
							.phone(contact.getPhone())
							.email(contact.getEmail())
							.mobilePhone(contact.getMobilePhone())
							.sex(Sex.valueOf(contact.getSex().name()))
							.contactInformation(contactInformation)
							.address(address)
							.workPlace(workplace)
							.build();

				}).collect(Collectors.toList()))
				.build();

		var events = EventList.builder()
<<<<<<< Updated upstream
				.events(submission.getEvents().stream().map(event -> {
					var address = Address.builder()
							.city(event.getAddress().getCity())
							.houseNumber(event.getAddress().getHouseNumber())
							.street(event.getAddress().getStreet())
							.zipCode(event.getAddress().getZipCode())
							.build();

					return Event.builder()
							.address(address)
							.name(event.getName())
							.phone(event.getPhone())
							.additionalInformation(event.getAdditionalInformation())
=======
				.endDate(LocalDate.ofInstant(submission.getEventsEndDate(), ZoneId.of("CET")))
				.startDate(LocalDate.ofInstant(submission.getEventsStartDate(), ZoneId.of("CET")))
				.events(submission.getEvents().stream().map(event -> {
					var eventAddress = event.getAddress();
					return Event.builder()
							.name(event.getName())
							.phone(event.getPhone())
							.additionalInformation(event.getAdditionalInformation())
							.address(Address.builder()
									.street(eventAddress.getStreet())
									.zipCode(eventAddress.getZipCode())
									.houseNumber(eventAddress.getHouseNumber())
									.city(eventAddress.getCity())
									.build())
>>>>>>> Stashed changes
							.build();
				}).collect(Collectors.toList()))
				.build();

		return ContactsAndEvents.builder()
				.dataProvider(contactsAndEventsDataProvider)
				.contacts(contacts)
				.events(events)
				.build();
	}
}
