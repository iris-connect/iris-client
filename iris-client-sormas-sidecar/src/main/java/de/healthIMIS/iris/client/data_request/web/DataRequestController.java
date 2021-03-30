package de.healthIMIS.iris.client.data_request.web;

import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.data_request.web.dto.DataRequestClient;
import de.healthIMIS.iris.client.data_request.web.dto.DataRequestDetails;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.management.ManagementFactory;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@AllArgsConstructor
public class DataRequestController {

    private DataRequestManagement dataRequestManagement;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/data-requests-client/locations", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataRequestDetails> createDataRequest(@Valid @RequestBody DataRequestClient request) {

        var result = dataRequestManagement.createLocationRequest(
                request.getExternalRequestId(),
                request.getName(),
                request.getStart().toInstant(),
                request.getEnd().toInstant(),
                Option.of(request.getRequestDetails()),
                request.getLocationId(),
                request.getProviderId()
        );

        var details = new DataRequestDetails();
        details.setCode(result.getId().toString());
        details.setRequestDetails(details.getRequestDetails());
        details.setName(result.getName());
        details.setStart(result.getRequestStart().atZone(ZoneId.of("UTC")));
        details.setEnd(result.getRequestEnd().atZone(ZoneId.of("UTC")));
        details.setRequestDetails(result.getRequestDetails());
        details.setStatus(DataRequestDetails.StatusEnum.DATA_REQUESTED);
        return ResponseEntity.ok(details);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/data-requests-client/locations")
    public ResponseEntity<DataRequestDetails> getDataRequests() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @GetMapping("/data-requests-client/locations/{code}")
    public ResponseEntity<DataRequestDetails> getDataRequestByCode() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


}
