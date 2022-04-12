package iris.client_bff.cases.rpc;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequestRepository;
import iris.client_bff.cases.DtoSupplier;
import iris.client_bff.cases.eps.CaseDataController;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.ContactPerson;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Event;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.web.CaseDataRequestController;
import iris.client_bff.cases.web.submission_dto.ContactPersonList;
import iris.client_bff.cases.web.submission_dto.EventList;
import iris.client_bff.core.serialization.DefuseJsonString;
import iris.client_bff.core.token.IdentifierToken;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

@IrisWebIntegrationTest
class CaseDataControllerIntegrationTest {

	@Autowired
	CaseDataController controller;

	@Autowired
	CaseDataRequestRepository requestRepo;

	@Autowired
	CaseDataRequestController requestController;

	@Autowired
	DtoSupplier dtoSupplier;

	@Test
	void submit_ok() {
		// prepare conditions
		String refId = "submit_ok";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		CaseDataRequest dataRequest = createRequest(refId, requestStart, requestEnd);

		// prepare data
		var contactPersonList = dtoSupplier.getContactPersonList(0, 2);
		Contacts contacts = Contacts.builder()
				.contactPersons(contactPersonList)
				.startDate(requestStart)
				.endDate(requestEnd)
				.build();

		var eventList = dtoSupplier.getEventList(0, 2);
		Events events = Events.builder()
				.events(eventList)
				.startDate(requestStart)
				.endDate(requestEnd)
				.build();

		CaseDataProvider dataProvider = CaseDataProvider.builder()
				.firstName("Max")
				.lastName("Mustermann")
				.build();

		// test
		var result = controller.submitContactAndEventData(dataRequest.getId().toString(), contacts, events, dataProvider);
		assertEquals("OK", result);

		// compare
		var detailsDTO = requestController.getDetails(dataRequest.getId()).getBody();
		assertNotNull(detailsDTO);
		var submissionData = detailsDTO.getSubmissionData();
		assertNotNull(submissionData);

		assertEquals(dataProvider.getFirstName(), submissionData.getDataProvider().getFirstName());
		assertEquals(dataProvider.getLastName(), submissionData.getDataProvider().getLastName());

		assertEquals(2, submissionData.getContacts().getContactPersons().size());
		assertEquals(2, submissionData.getEvents().getEvents().size());

		assertThat(submissionData.getContacts()).asString().doesNotContain(DefuseJsonString.INVALID_INPUT_STRING);

		// test repeated data submission is rejected
		contacts.setContactPersons(dtoSupplier.getContactPersonList(2, 3));
		events.setEvents(dtoSupplier.getEventList(2, 3));
		result = controller.submitContactAndEventData(dataRequest.getId().toString(), contacts, events, dataProvider);
		assertNotEquals("OK", result);
	}

	@Test
	void submit_ok_with_empty_events_and_empty_contacts() {
		// prepare conditions
		String refId = "submit_ok_with_empty_events_and_empty_contacts";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		CaseDataRequest dataRequest = createRequest(refId, requestStart, requestEnd);

		// prepare data
		Contacts contacts = Contacts.builder().build();
		Events events = Events.builder().build();

		CaseDataProvider dataProvider = CaseDataProvider.builder()
				.firstName("Max")
				.lastName("Mustermann")
				.build();

		// test
		var result = controller.submitContactAndEventData(dataRequest.getId().toString(), contacts, events, dataProvider);
		assertEquals("OK", result);

		// compare
		var detailsDTO = requestController.getDetails(dataRequest.getId()).getBody();
		assertNotNull(detailsDTO);
		var submissionData = detailsDTO.getSubmissionData();
		assertNotNull(submissionData);

		assertEquals(dataProvider.getFirstName(), submissionData.getDataProvider().getFirstName());
		assertEquals(dataProvider.getLastName(), submissionData.getDataProvider().getLastName());

		assertEquals(ContactPersonList.builder().build(), submissionData.getContacts());
		assertEquals(EventList.builder().build(), submissionData.getEvents());

		// test repeated data submission is rejected
		contacts.setContactPersons(dtoSupplier.getContactPersonList(2, 3));
		events.setEvents(dtoSupplier.getEventList(2, 3));
		result = controller.submitContactAndEventData(dataRequest.getId().toString(), contacts, events, dataProvider);
		assertNotEquals("OK", result);
	}

	@Test
	void submitError_block_to_many_contacts() {

		// prepare conditions
		String refId = "submitError_block_to_many_contacts";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		CaseDataRequest dataRequest = createRequest(refId, requestStart, requestEnd);
		requestRepo.save(dataRequest);

		// prepare data
		var token = dataRequest.getId().toString();

		var contactPersonList = Stream.generate(() -> dtoSupplier.getContactPersonList(0, 2))
				.flatMap(List<ContactPerson>::stream)
				.limit(1020)
				.toList();

		var contacts = Contacts.builder().contactPersons(contactPersonList).startDate(requestStart).endDate(requestEnd)
				.build();

		var eventList = dtoSupplier.getEventList(0, 2);
		var events = Events.builder().events(eventList).startDate(requestStart).endDate(requestEnd).build();

		CaseDataProvider dataProvider = CaseDataProvider.builder().firstName("Max").lastName("Mustermann").build();

		// test
		assertThrows(ResponseStatusException.class, () -> {
			controller.submitContactAndEventData(token, contacts, events, dataProvider);
		});
	}

	@Test
	void submitError_block_to_many_events() {

		// prepare conditions
		String refId = "submitError_block_to_many_events";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		CaseDataRequest dataRequest = createRequest(refId, requestStart, requestEnd);
		requestRepo.save(dataRequest);

		// prepare data
		var token = dataRequest.getId().toString();

		var contactPersonList = dtoSupplier.getContactPersonList(0, 2);
		var contacts = Contacts.builder().contactPersons(contactPersonList).startDate(requestStart).endDate(requestEnd)
				.build();

		var eventList = Stream.generate(() -> dtoSupplier.getEventList(0, 2))
				.flatMap(List<Event>::stream)
				.limit(1020)
				.toList();

		var events = Events.builder().events(eventList).startDate(requestStart).endDate(requestEnd).build();

		CaseDataProvider dataProvider = CaseDataProvider.builder().firstName("Max").lastName("Mustermann").build();

		// test
		assertThrows(ResponseStatusException.class, () -> {
			controller.submitContactAndEventData(token, contacts, events, dataProvider);
		});
	}

	private CaseDataRequest createRequest(String refId, Instant requestStart, Instant requestEnd) {

		var token = IdentifierToken.builder()
				.readableToken(refId)
				.connectionAuthorizationToken("CAT")
				.dataAuthorizationToken("DAT")
				.build();

		var dataRequest = CaseDataRequest.builder()
				.refId(refId)
				.requestStart(requestStart)
				.requestEnd(requestEnd)
				.identifierToken(token)
				.build();

		requestRepo.save(dataRequest);

		return dataRequest;
	}
}
