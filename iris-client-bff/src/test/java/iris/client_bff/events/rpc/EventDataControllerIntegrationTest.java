package iris.client_bff.events.rpc;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.web.dto.Address;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.eps.EventDataController;
import iris.client_bff.events.eps.JsonRpcClientDto;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IrisWebIntegrationTest
public class EventDataControllerIntegrationTest {

    @Autowired
    EventDataController controller;

    @Autowired
    EventDataRequestRepository requestRepo;

    @Autowired
    EventDataSubmissionRepository submissionRepo;

    @Test
    public void submitGuestList_ok() {
        // prepare conditions
        String refId = "submitGuestList_ok";
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
                                .city("Stadt")
                                .zipCode("0815")
                                .houseNumber("1")
                                .street("Straße").build()).build())
                //.guests()
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
    public void submitGuestList_incompleteAddress() {
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
                                .street("Straße").build()).build())
                //.guests()
                .build();

        // test
        try {
            var result = controller.submitGuestList(clientDto, UUID.fromString(dataRequest.getId().toString()), guestList);
        } catch (Exception e) {
            assertEquals(e.getClass(), ResponseStatusException.class);
            assertTrue(e.getMessage().contains("Eingabedaten sind ungültig") && e.getMessage().contains("dataProvider.address.city"));
        }
    }
}
