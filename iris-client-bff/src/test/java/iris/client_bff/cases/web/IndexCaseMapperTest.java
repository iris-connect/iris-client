package iris.client_bff.cases.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.DtoSupplier;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.CaseDataSubmissionMapper;
import iris.client_bff.cases.CaseDataSubmissionMapperImpl;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.ContactCategory;
import iris.client_bff.cases.eps.dto.ContactInformation;
import iris.client_bff.cases.eps.dto.ContactPerson;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.eps.dto.WorkPlace;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.entities.UserAccount;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndexCaseMapperTest {

	DtoSupplier dtoSupplier = new DtoSupplier();

	CaseDataSubmissionMapper mapper = new CaseDataSubmissionMapperImpl();

	Instant START = Instant.now();
	Instant END = Instant.now().plus(1, ChronoUnit.DAYS);

	@BeforeEach
	void init() {
		dtoSupplier.init();
	}

	@Test
	void mapDetailed() {

		var userService = mock(UserDetailsServiceImpl.class);
		when(userService.findByUuid(any()))
				.thenReturn(Optional.of(new UserAccount().setFirstName("Max").setLastName("Muster")));

		var request = getRequest();
		var mapped = IndexCaseMapper.mapDetailed(request, userService);
		var name = "Max Muster";

		var expected = IndexCaseDetailsDTO.builder()
				.name("request_name")
				.status(IndexCaseStatusDTO.ABORTED)
				.externalCaseId("my-ref-id")
				.start(START)
				.end(END)
				.comment("a-comment-here")
				.caseId(request.getId().toString())
				.createdBy(name)
				.lastModifiedBy(name)
				.build();

		assertEquals(expected, mapped);
	}

	@Test
	void map() {
		var request = getRequest();
		var mapped = IndexCaseMapper.map(request);

		var expected = IndexCaseDTO.builder()
				.name("request_name")
				.status(IndexCaseStatusDTO.ABORTED)
				.externalCaseId("my-ref-id")
				.start(START)
				.end(END)
				.comment("a-comment-here")
				.caseId(request.getId().toString())
				.build();

		assertEquals(expected, mapped);
	}

	@Test
	void mapDataSubmission() {
		var submission = getSubmission();
		var mapped = IndexCaseMapper.mapDataSubmission(submission);

		assertEquals(LocalDate.ofInstant(START, ZoneId.of("CET")), mapped.getContacts().getStartDate());
		assertEquals(LocalDate.ofInstant(END, ZoneId.of("CET")), mapped.getContacts().getEndDate());

		var contact = submission.getContacts().iterator().next();
		var expectedContact = mapped.getContacts().getContactPersons().get(0);
		assertEquals(contact.getFirstName(), expectedContact.getFirstName());
		assertEquals(contact.getLastName(), expectedContact.getLastName());
		assertEquals(contact.getDateOfBirth(), expectedContact.getDateOfBirth());
		assertEquals(contact.getSex(), expectedContact.getSex());
		assertEquals(contact.getPhone(), expectedContact.getPhone());
		assertEquals(contact.getMobilePhone(), expectedContact.getMobilePhone());
		assertEquals(contact.getEmail(), expectedContact.getEmail());
		assertEquals(contact.getContactCategory().toString(),
				expectedContact.getContactInformation().getContactCategory().toString());
		assertEquals(contact.getFirstContactDate(), expectedContact.getContactInformation().getFirstContactDate());
		assertEquals(contact.getLastContactDate(), expectedContact.getContactInformation().getLastContactDate());
		assertEquals(contact.getBasicConditions(), expectedContact.getContactInformation().getBasicConditions());
		assertEquals(contact.getAddress().getStreet(), expectedContact.getAddress().getStreet());
		assertEquals(contact.getAddress().getHouseNumber(), expectedContact.getAddress().getHouseNumber());
		assertEquals(contact.getAddress().getZipCode(), expectedContact.getAddress().getZipCode());
		assertEquals(contact.getAddress().getCity(), expectedContact.getAddress().getCity());
		assertEquals(contact.getWorkplaceName(), expectedContact.getWorkPlace().getName());
		assertEquals(contact.getWorkplacePointOfContact(), expectedContact.getWorkPlace().getPointOfContact());
		assertEquals(contact.getWorkplacePhone(), expectedContact.getWorkPlace().getPhone());
		assertEquals(contact.getWorkplaceAddress().getStreet(), expectedContact.getWorkPlace().getAddress().getStreet());
		assertEquals(contact.getWorkplaceAddress().getHouseNumber(),
				expectedContact.getWorkPlace().getAddress().getHouseNumber());
		assertEquals(contact.getWorkplaceAddress().getZipCode(), expectedContact.getWorkPlace().getAddress().getZipCode());
		assertEquals(contact.getWorkplaceAddress().getCity(), expectedContact.getWorkPlace().getAddress().getCity());

		assertEquals(LocalDate.ofInstant(START, ZoneId.of("CET")), mapped.getEvents().getStartDate());
		assertEquals(LocalDate.ofInstant(END, ZoneId.of("CET")), mapped.getEvents().getEndDate());

		var event = submission.getEvents().iterator().next();
		var expectedEvent = mapped.getEvents().getEvents().get(0);

		assertEquals(event.getName(), expectedEvent.getName());
		assertEquals(event.getPhone(), expectedEvent.getPhone());
		assertEquals(event.getAdditionalInformation(), expectedEvent.getAdditionalInformation());
		assertEquals(event.getAddress().getStreet(), expectedEvent.getAddress().getStreet());
		assertEquals(event.getAddress().getHouseNumber(), expectedEvent.getAddress().getHouseNumber());
		assertEquals(event.getAddress().getZipCode(), expectedEvent.getAddress().getZipCode());
		assertEquals(event.getAddress().getCity(), expectedEvent.getAddress().getCity());

		assertEquals(submission.getDataProvider().getFirstName(), mapped.getDataProvider().getFirstName());
		assertEquals(submission.getDataProvider().getLastName(), mapped.getDataProvider().getLastName());
		assertEquals(submission.getDataProvider().getDateOfBirth(), mapped.getDataProvider().getDateOfBirth());
	}

	private CaseDataSubmission getSubmission() {
		List<ContactPerson> contactPeople = dtoSupplier.getContactPersonList(0, 1);

		contactPeople.get(0).setAddress(dtoSupplier.getAddress(0));
		contactPeople.get(0).setContactInformation(ContactInformation.builder()
				.firstContactDate(START)
				.lastContactDate(END)
				.basicConditions("Test1")
				.contactCategory(ContactCategory.HIGH_RISK)
				.build());
		contactPeople.get(0).setWorkPlace(WorkPlace.builder()
				.address(dtoSupplier.getAddress(2))
				.name("Company")
				.phone("1234")
				.pointOfContact("Mensa")
				.build());

		Contacts contacts = Contacts.builder()
				.contactPersons(contactPeople)
				.startDate(START)
				.endDate(END)
				.build();

		var contactSet = contacts.getContactPersons().stream()
				.map(mapper::fromContactPersonDto)
				.collect(Collectors.toSet());

		Events events = Events.builder()
				.events(dtoSupplier.getEventList(0, 1))
				.startDate(START)
				.endDate(END)
				.build();

		var eventSet = events.getEvents().stream()
				.map(mapper::fromEventDto)
				.collect(Collectors.toSet());

		CaseDataProvider dataProvider = CaseDataProvider.builder()
				.firstName("Max")
				.lastName("Mustermann")
				.build();

		var caseDataProvider = mapper.fromCaseDataProviderDto(dataProvider);

		return new CaseDataSubmission(getRequest(),
				contactSet,
				START, END,
				eventSet,
				START, END,
				caseDataProvider);
	}

	private CaseDataRequest getRequest() {
		CaseDataRequest dataRequest = new CaseDataRequest();
		dataRequest.setName("request_name");
		dataRequest.setStatus(Status.ABORTED);
		dataRequest.setRefId("my-ref-id");
		dataRequest.setRequestStart(START);
		dataRequest.setRequestEnd(END);
		dataRequest.setComment("a-comment-here");
		dataRequest.setHdUserId("a-hd-user-id");
		return dataRequest;
	}
}
