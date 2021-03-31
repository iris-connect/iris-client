package de.healthIMIS.iris.client.data_request.web;

import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.data_request.web.dto.DataRequestClient;
import de.healthIMIS.iris.client.data_request.web.dto.DataRequestDetails;
import de.healthIMIS.iris.client.data_request.web.dto.ExistingDataRequestClientWithLocation;
import de.healthIMIS.iris.client.data_request.web.dto.ExistingDataRequestClientWithLocationList;
import de.healthIMIS.iris.client.data_request.web.dto.Guest;
import de.healthIMIS.iris.client.data_request.web.dto.GuestListDataProvider;
import de.healthIMIS.iris.client.data_request.web.dto.LocationInformation;
import de.healthIMIS.iris.client.data_submission.DataSubmissionRepository;
import de.healthIMIS.iris.client.data_submission.entities.DataSubmission;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DataRequestController {

	private DataRequestManagement dataRequestManagement;
	private DataSubmissionRepository submissionRepo;

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
				request.getProviderId());

		return ResponseEntity.ok(map(result));
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/data-requests-client/locations")
	public ResponseEntity<ExistingDataRequestClientWithLocationList> getDataRequests() {
		var response = dataRequestManagement.getAll().stream().map(request -> {
			var mapped = modelMapper.map(request, ExistingDataRequestClientWithLocation.class);
			mapped.setCode(request.getId().toString());
			mapped.setStart(request.getRequestStart().atZone(ZoneId.of("UTC")));
			mapped.setEnd(request.getRequestEnd().atZone(ZoneId.of("UTC")));
			mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
			mapped.setLastUpdatedAt(request.getLastModifiedAt().atZone(ZoneId.of("UTC")));
			mapped.setRequestedAt(request.getCreatedAt().atZone(ZoneId.of("UTC")));
			mapped.setExternalRequestId(request.getRefId());
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

			var requestDetails = map(dataRequest.get());

			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private DataRequestDetails map(de.healthIMIS.iris.client.data_request.DataRequest request) {
		var mapped = modelMapper.map(request, DataRequestDetails.class);
		mapped.setCode(request.getId().toString());
		// TODO this is not returning UTC check whats happening
		mapped.setStart(request.getRequestStart().atZone(ZoneId.of("UTC")));
		mapped.setEnd(request.getRequestEnd().atZone(ZoneId.of("UTC")));
		mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
		mapped.setLastModifiedAt(request.getLastModifiedAt().atZone(ZoneId.of("UTC")));
		mapped.setRequestedAt(request.getCreatedAt().atZone(ZoneId.of("UTC")));
		mapped.setExternalRequestId(request.getRefId());
		return mapped;
	}

	/**
	 * @param request
	 * @param requestDetails
	 */
	private void addSubmissionsToRequest(DataRequest request, DataRequestDetails requestDetails) {

		submissionRepo.findAllByRequest(request)
				.get()
				.findFirst()
				.ifPresent(it -> addSubmissionToRequest(requestDetails, it));
	}

	/**
	 * @param requestDetails
	 * @param submission
	 * @return
	 */
	private void addSubmissionToRequest(DataRequestDetails requestDetails, DataSubmission submission) {

		requestDetails.setAdditionalInformation(submission.getAdditionalInformation());
		requestDetails.setStartDate(submission.getStartDate().toZonedDateTime());
		requestDetails.setEndDate(submission.getEndDate().toZonedDateTime());
		requestDetails.setDataProvider(modelMapper.map(submission.getDataProvider(), GuestListDataProvider.class));

		var guests = submission.getGuests()
				.stream()
				.map(it -> modelMapper.map(it, Guest.class))
				.collect(Collectors.toList());
		requestDetails.setGuests(guests);
	}
}
