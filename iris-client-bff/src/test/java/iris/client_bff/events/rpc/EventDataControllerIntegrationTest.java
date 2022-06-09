package iris.client_bff.events.rpc;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.api.dto.AddressWithDefuseData;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.eps.EventDataController;
import iris.client_bff.events.eps.JsonRpcClientDto;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.model.Location.LocationIdentifier;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

@IrisWebIntegrationTest
@RequiredArgsConstructor
class EventDataControllerIntegrationTest {

	final EventDataController controller;
	final EventDataRequestRepository requestRepo;
	final EventDataSubmissionRepository submissionRepo;
	final Faker faker;

	@BeforeEach
	void init() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	void submitGuestList_ok() {
		// prepare conditions
		String refId = "submitGuestList_ok";
		String providerName = "provider_1";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		var providerId = faker.idNumber().valid();
		var location = new Location(LocationIdentifier.random(), providerId, faker.idNumber().valid(), null,
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
						.address(AddressWithDefuseData.builder()
								.city("Stadt")
								.zipCode("0815")
								.houseNumber("1")
								.street("Straße").build())
						.build())
				// .guests()
				.build();

		// test
		var result = controller.submitGuestList(clientDto, dataRequest.getId(), guestList);
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
						.address(AddressWithDefuseData.builder()
								.houseNumber("1")
								.street("Straße").build())
						.build())
				// .guests()
				.build();

		// test
		var e = assertThrows(ConstraintViolationException.class,
				() -> controller.submitGuestList(clientDto, dataRequest.getId(), guestList));

		assertThat(e.getMessage()).contains("must not be blank")
				.contains("dataProvider.address.city");
	}

	@Test
	void submitError_block_to_many_guests() {
		// prepare conditions
		String refId = "submitGuestList_error";
		String providerName = "provider_1";
		Instant requestStart = Instant.now().minus(14, ChronoUnit.DAYS);
		Instant requestEnd = Instant.now();
		EventDataRequest dataRequest = EventDataRequest.builder()
				.refId(refId).requestStart(requestStart).requestEnd(requestEnd)
				.build();
		requestRepo.save(dataRequest);

		// prepare data
		var token = dataRequest.getId();
		var clientDto = new JsonRpcClientDto();
		clientDto.setName(refId);

		var guests = new ArrayList<Guest>(10010);
		for (int i = 0; i < 10010; i++) {
			guests.add(Guest.builder().firstName("fn").lastName("ln").build());
		}

		var guestList = GuestList.builder()
				.startDate(requestStart)
				.endDate(requestEnd)
				.dataProvider(GuestListDataProvider.builder().name(providerName).build())
				.guests(guests)
				.build();

		// test
		assertThrows(ConstraintViolationException.class, () -> {
			controller.submitGuestList(clientDto, token, guestList);
		});
	}
}
