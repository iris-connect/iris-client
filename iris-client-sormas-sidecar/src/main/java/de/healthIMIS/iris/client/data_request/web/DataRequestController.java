package de.healthIMIS.iris.client.data_request.web;

import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.data_request.web.dto.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class DataRequestController {

    private DataRequestManagement dataRequestManagement;

    private ModelMapper modelMapper;

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

        return ResponseEntity.ok(map(result));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/data-requests-client/locations")
    public ResponseEntity<ExistingDataRequestClientWithLocationList> getDataRequests() {
        var response = dataRequestManagement.getAll().stream().map(request -> {
            var mapped = modelMapper.map(request, ExistingDataRequestClientWithLocation.class);
            mapped.setCode(request.getId().toString());
            mapped.setStatus(ExistingDataRequestClientWithLocation.StatusEnum.DATA_REQUESTED);
            mapped.setStart(request.getRequestStart().atZone(ZoneId.of("UTC")));
            mapped.setEnd(request.getRequestEnd().atZone(ZoneId.of("UTC")));
            mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
            return mapped;
        })
                .collect(Collectors.toList());
        var res = new ExistingDataRequestClientWithLocationList();
        res.setDataRequests(response);
        return ResponseEntity.of(Optional.of(res));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @GetMapping("/data-requests-client/locations/{code}")
    public ResponseEntity<DataRequestDetails> getDataRequestByCode(@PathVariable String code) {
        var dataRequest = dataRequestManagement.findById(code);
        if (dataRequest.isPresent()) {
            return ResponseEntity.of(Optional.of(map(dataRequest.get())));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private DataRequestDetails map(de.healthIMIS.iris.client.data_request.DataRequest request) {
        var mapped = modelMapper.map(request, DataRequestDetails.class);
        mapped.setCode(request.getId().toString());
        mapped.setStatus(DataRequestDetails.StatusEnum.DATA_REQUESTED);
        mapped.setStart(request.getRequestStart().atZone(ZoneId.of("UTC")));
        mapped.setEnd(request.getRequestEnd().atZone(ZoneId.of("UTC")));
        mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
        return mapped;
    }


}
