package de.healthIMIS.iris.client.data_request.web;

import de.healthIMIS.iris.client.data_request.web.dto.DataRequestClient;
import de.healthIMIS.iris.client.data_request.web.dto.DataRequestDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class DataRequestController {


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/data-requests-client/locations", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataRequestDetails> createDataRequest(@Valid @RequestBody DataRequestClient request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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
