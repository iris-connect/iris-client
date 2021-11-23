package iris.client_bff.events.rpc;

import static org.junit.jupiter.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.web.dto.Address;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.eps.EventDataController;
import iris.client_bff.events.eps.JsonRpcClientDto;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.model.Location.LocationIdentifier;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.github.javafaker.Faker;

@IrisWebIntegrationTest
@RequiredArgsConstructor
class EventDataControllerIntegrationTest {

	final EventDataController controller;
	final EventDataRequestRepository requestRepo;
	final EventDataSubmissionRepository submissionRepo;
	final Faker faker;

	@Test
	void submitGuestList_ok() {
		// prepare conditions
		String refId = "submitGuestList_ok";
		String providerName = "provider_1";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		var providerId = faker.idNumber().valid();
		var location = new Location(new LocationIdentifier(), providerId, faker.idNumber().valid(), null,
				null, null, null, null, null, null, null, null, null);

		var dataRequest = EventDataRequest.builder()
				.refId(refId)
				.requestStart(requestStart)
				.requestEnd(requestEnd)
				.location(location)
				.build();

		requestRepo.save(dataRequest);

		// prepare data
		JsonRpcClientDto clientDto = new JsonRpcClientDto();
		clientDto.setName(providerId);
		GuestList guestList = GuestList.builder()
				.startDate(requestStart)
				.endDate(requestEnd)
				.dataProvider(GuestListDataProvider.builder()
						.name(providerName)
						.address(Address.builder()
								.city("Stadt")
								.zipCode("0815")
								.houseNumber("1")
								.street("Straße").build())
						.build())
				// .guests()
				.build();

		// test
		var result = controller.submitGuestList(clientDto, UUID.fromString(dataRequest.getId().toString()), guestList);
		assertEquals("OK", result);

		var submissionList = submissionRepo.findAllByRequest(dataRequest).toList();
		assertEquals(1, submissionList.size());
		var submission = submissionList.get(0);
		assertEquals(requestStart.truncatedTo(ChronoUnit.MILLIS), submission.getStartDate().truncatedTo(ChronoUnit.MILLIS));
		assertEquals(requestEnd.truncatedTo(ChronoUnit.MILLIS), submission.getEndDate().truncatedTo(ChronoUnit.MILLIS));
		assertEquals(providerName, submission.getDataProvider().getName());
	}

	@Test
	void submitGuestList_incompleteAddress() {
		// prepare conditions
		String refId = "submitGuestList_incompleteAddress";
		String providerName = "provider_1";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		EventDataRequest dataRequest = EventDataRequest.builder()
				.refId(refId).requestStart(requestStart).requestEnd(requestEnd)
				.build();
		requestRepo.save(dataRequest);

		// prepare data
		JsonRpcClientDto clientDto = new JsonRpcClientDto();
		clientDto.setName(refId);
		GuestList guestList = GuestList.builder()
				.startDate(requestStart)
				.endDate(requestEnd)
				.dataProvider(GuestListDataProvider.builder()
						.name(providerName)
						.address(Address.builder()
								.houseNumber("1")
								.street("Straße").build())
						.build())
				// .guests()
				.build();

		// test
		var e = assertThrows(ResponseStatusException.class,
				() -> controller.submitGuestList(clientDto, UUID.fromString(dataRequest.getId().toString()), guestList));

		assertTrue(e.getMessage().contains("Eingabedaten sind ungültig")
				&& e.getMessage().contains("dataProvider.address.city"));
	}
}
