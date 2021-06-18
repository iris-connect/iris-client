package iris.client_bff.cases.rpc;

import static org.junit.jupiter.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequestRepository;
import iris.client_bff.cases.eps.CaseDataController;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Events;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IrisWebIntegrationTest
class CaseDataControllerIntegrationTest {

	@Autowired
	CaseDataController controller;

	@Autowired
	CaseDataRequestRepository requestRepo;

	@BeforeEach
	void setUp() {}

	@Test
	void submit_ok() throws Exception {
		// prepare conditions
		String refId = "submit_ok";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		CaseDataRequest dataRequest = CaseDataRequest.builder()
				.refId(refId).requestStart(requestStart).requestEnd(requestEnd)
				.build();
		requestRepo.save(dataRequest);

		Contacts contacts = Contacts.builder()
				.contactPersons(new ArrayList<>())
				.startDate(requestStart)
				.endDate(requestEnd)
				.build();

		Events events = Events.builder()
				.events(new ArrayList<>())
				.startDate(requestStart)
				.endDate(requestEnd)
				.build();

		CaseDataProvider dataProvider = CaseDataProvider.builder()
				.firstName("Max")
				.firstName("Mustermann")
				.dateOfBirth(Instant.now()/*parse("12.12.1978")*/)
				.build();

		var result = controller.submitContactAndEventData(dataRequest.getId().getRequestId(), contacts, events, dataProvider);
		assertEquals("OK", result);
	}

}
