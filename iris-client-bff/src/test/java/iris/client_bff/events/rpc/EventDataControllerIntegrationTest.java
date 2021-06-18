package iris.client_bff.events.rpc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.eps.EventDataController;
import iris.client_bff.events.eps.JsonRpcClientDto;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
                .dataProvider(GuestListDataProvider.builder().name(providerName).build())
                //.guests()
                .build();

        // test
        var result = controller.submitGuestList(clientDto, UUID.fromString(dataRequest.getId().toString()), guestList);
        assertEquals("OK", result);

        var submissionList = submissionRepo.findAllByRequest(dataRequest).toList();
        assertEquals(1, submissionList.size());
        var submission = submissionList.get(0);
        assertEquals(requestStart, submission.getStartDate());
        assertEquals(requestEnd, submission.getEndDate());
        assertEquals(providerName, submission.getDataProvider().getName());
    }
}
