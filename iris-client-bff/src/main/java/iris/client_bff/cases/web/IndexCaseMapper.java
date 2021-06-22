package iris.client_bff.cases.web;

import static lombok.AccessLevel.PRIVATE;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
import iris.client_bff.cases.web.submission_dto.ContactCategory;
import iris.client_bff.cases.web.submission_dto.ContactPerson;
import iris.client_bff.cases.web.submission_dto.ContactPersonAllOfContactInformation;
import iris.client_bff.cases.web.submission_dto.ContactPersonAllOfWorkPlace;
import iris.client_bff.cases.web.submission_dto.ContactPersonList;
import iris.client_bff.cases.web.submission_dto.ContactsAndEvents;
import iris.client_bff.cases.web.submission_dto.ContactsAndEventsDataProvider;
import iris.client_bff.cases.web.submission_dto.EventList;
import iris.client_bff.core.Sex;
import iris.client_bff.core.web.dto.Address;
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
							.contactCategory(ContactCategory.valueOf(contact.getContactCategory()))
							.basicConditions(contact.getBasicConditions())
							.firstContactDate(contact.getFirstContactDate())
							.lastContactDate(contact.getLastContactDate())
							.build();

					var workplace = ContactPersonAllOfWorkPlace.builder()
							.name(contact.getWorkplaceName())
							.pointOfContact(contact.getWorkplacePointOfContact())
							.phone("TODO") // TODO
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
							.sex(Sex.valueOf(contact.getSubmission().toString()))
							.contactInformation(contactInformation)
							.address(address)
							.workPlace(workplace)
							.build();

				}).collect(Collectors.toList()))
				.build();
		EventList events;
		return ContactsAndEvents.builder()
				.dataProvider(contactsAndEventsDataProvider)
				.contacts(contacts)
				.events(events)
				.build();
	}
}
