package iris.client_bff.cases.rpc;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequestRepository;
import iris.client_bff.cases.eps.CaseDataController;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Event;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.web.CaseDataRequestController;
import iris.client_bff.cases.web.submission_dto.ContactPersonList;
import iris.client_bff.cases.web.submission_dto.EventList;
import iris.client_bff.ui.messages.ErrorMessages;
import iris.client_bff.utils.DtoSupplier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
		CaseDataRequest dataRequest = CaseDataRequest.builder()
				.refId(refId).requestStart(requestStart).requestEnd(requestEnd)
				.build();
		requestRepo.save(dataRequest);

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
		var result = controller.submitContactAndEventData(dataRequest.getId().getRequestId().toString(), contacts, events, dataProvider);
		assertEquals("OK", result);

		// compare
		var detailsDTO = requestController.getDetails(dataRequest.getId().getRequestId()).getBody();
		assertNotNull(detailsDTO);
		var submissionData = detailsDTO.getSubmissionData();
		assertNotNull(submissionData);

		assertEquals(dataProvider.getFirstName(), submissionData.getDataProvider().getFirstName());
		assertEquals(dataProvider.getLastName(), submissionData.getDataProvider().getLastName());

		assertEquals(2, submissionData.getContacts().getContactPersons().size());
		assertEquals(2, submissionData.getEvents().getEvents().size());

		assertThat(submissionData.getContacts()).asString().doesNotContain(ErrorMessages.INVALID_INPUT_STRING);
		
		// test repeated data submission is rejected
		contacts.setContactPersons(dtoSupplier.getContactPersonList(2,3));
		events.setEvents(dtoSupplier.getEventList(2,3));
		result = controller.submitContactAndEventData(dataRequest.getId().getRequestId().toString(), contacts, events, dataProvider);
		assertNotEquals("OK", result);

	}

	@Test
	void submit_ok_with_empty_events_and_empty_contacts() {
		// prepare conditions
		String refId = "submit_ok_with_empty_events_and_empty_contacts";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		CaseDataRequest dataRequest = CaseDataRequest.builder()
				.refId(refId).requestStart(requestStart).requestEnd(requestEnd)
				.build();
		requestRepo.save(dataRequest);

		// prepare data
		//var contactPersonList = dtoSupplier.getContactPersonList(0, 2);
		Contacts contacts = Contacts.builder().build();
//		Contacts contacts = Contacts.builder()
//				.contactPersons(contactPersonList)
//				.startDate(requestStart)
//				.endDate(requestEnd)
//				.build();

		Events events = Events.builder().build();

		CaseDataProvider dataProvider = CaseDataProvider.builder()
				.firstName("Max")
				.lastName("Mustermann")
				.build();

		// test
		var result = controller.submitContactAndEventData(dataRequest.getId().getRequestId().toString(), contacts, events, dataProvider);
		assertEquals("OK", result);

		// compare
		var detailsDTO = requestController.getDetails(dataRequest.getId().getRequestId()).getBody();
		assertNotNull(detailsDTO);
		var submissionData = detailsDTO.getSubmissionData();
		assertNotNull(submissionData);

		assertEquals(dataProvider.getFirstName(), submissionData.getDataProvider().getFirstName());
		assertEquals(dataProvider.getLastName(), submissionData.getDataProvider().getLastName());

		assertEquals(ContactPersonList.builder().build(), submissionData.getContacts());
		assertEquals(EventList.builder().build(), submissionData.getEvents());

		// test repeated data submission is rejected
		contacts.setContactPersons(dtoSupplier.getContactPersonList(2,3));
		events.setEvents(dtoSupplier.getEventList(2,3));
		result = controller.submitContactAndEventData(dataRequest.getId().getRequestId().toString(), contacts, events, dataProvider);
		assertNotEquals("OK", result);
	}

}
