package iris.client_bff.cases.rpc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequestRepository;
import iris.client_bff.cases.eps.CaseDataController;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.web.CaseDataRequestController;
import iris.client_bff.utils.DtoSupplier;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

	@BeforeEach
	void setUp() {}

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
		var contactPersonList = dtoSupplier.getContactPersonList(2);
		Contacts contacts = Contacts.builder()
				.contactPersons(contactPersonList)
				.startDate(requestStart)
				.endDate(requestEnd)
				.build();

		var eventList = dtoSupplier.getEventList(2);
		Events events = Events.builder()
				.events(eventList)
				.startDate(requestStart)
				.endDate(requestEnd)
				.build();

		CaseDataProvider dataProvider = CaseDataProvider.builder()
				.firstName("Max")
				.lastName("Mustermann")
				.dateOfBirth(Instant.parse("1978-12-12T10:15:30Z"))
				.build();

		// test
		var result = controller.submitContactAndEventData(dataRequest.getId().getRequestId(), contacts, events, dataProvider);
		assertEquals("OK", result);

		// compare
		var detailsDTO = requestController.getDetails(dataRequest.getId().getRequestId()).getBody();
		assertNotNull(detailsDTO);
		var submissionData = detailsDTO.getSubmissionData();
		assertNotNull(submissionData);

		assertEquals(dataProvider.getFirstName(), submissionData.getDataProvider().getFirstName());
		assertEquals(dataProvider.getLastName(), submissionData.getDataProvider().getLastName());
		assertEquals(LocalDate.ofInstant(dataProvider.getDateOfBirth(), ZoneId.of("CET")), submissionData.getDataProvider().getDateOfBirth());
	}

}
