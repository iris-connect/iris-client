package iris.client_bff.events.web;

import io.vavr.control.Option;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.DataRequestClient;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.events.web.dto.EventUpdateDTO;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocation;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocationList;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import iris.client_bff.events.web.dto.LocationInformation;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/data-requests-client/events")
@RestController
@AllArgsConstructor
public class EventDataRequestController {

	private final EventDataRequestService dataRequestService;
	private final EventDataSubmissionRepository submissionRepo;

	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DataRequestDetails> createDataRequest(@Valid @RequestBody DataRequestClient request) {

		var result = dataRequestService.createDataRequest(
			request.getExternalRequestId(),
			request.getName(),
			request.getStart(),
			Option.of(request.getEnd()),
			Option.of(request.getComment()),
			Option.of(request.getRequestDetails()),
			Option.none(),
			Option.of(request.getLocationId()),
			Option.of(request.getProviderId()));

		return ResponseEntity.ok(mapDataRequestDetails(result));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ExistingDataRequestClientWithLocationList> getDataRequests() {
		var response = dataRequestService.getAll().stream().map(request -> {
			return mapExistingDataRequestClientWithLocation(request);
		}).collect(Collectors.toList());
		var res = ExistingDataRequestClientWithLocationList.builder().dataRequests(response).build();
		return ResponseEntity.of(Optional.of(res));
	}

	@GetMapping("/{code}")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<DataRequestDetails> getDataRequestByCode(@PathVariable UUID code) {
		var dataRequest = dataRequestService.findById(code);
		if (dataRequest.isPresent()) {

			DataRequestDetails requestDetails = mapDataRequestDetails(dataRequest.get());

			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{code}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DataRequestDetails> update(@PathVariable UUID code, @RequestBody EventUpdateDTO patch) {

		var dataRequest = dataRequestService.findById(code);
		if (dataRequest.isPresent()) {
			var updated = dataRequestService.update(dataRequest.get(), patch);
			DataRequestDetails requestDetails = mapDataRequestDetails(updated);

			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private DataRequestDetails mapDataRequestDetails(EventDataRequest request) {
		DataRequestDetails mapped = modelMapper.map(request, DataRequestDetails.class);
		mapped.setCode(request.getId().toString());
		mapped.setStart(request.getRequestStart());
		mapped.setEnd(request.getRequestEnd());
		mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
		mapped.setLastModifiedAt(request.getLastModifiedAt());
		mapped.setRequestedAt(request.getCreatedAt());
		mapped.setExternalRequestId(request.getRefId());
		return mapped;
	}

	private ExistingDataRequestClientWithLocation mapExistingDataRequestClientWithLocation(EventDataRequest request) {
		ExistingDataRequestClientWithLocation mapped = modelMapper.map(request, ExistingDataRequestClientWithLocation.class);
		mapped.setCode(request.getId().toString());
		mapped.setStart(request.getRequestStart());
		mapped.setEnd(request.getRequestEnd());
		mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
		mapped.setLastUpdatedAt(request.getLastModifiedAt());
		mapped.setRequestedAt(request.getCreatedAt());
		mapped.setExternalRequestId(request.getRefId());
		mapped.setRequestDetails(request.getRequestDetails());
		mapped.setName(request.getName());
		return mapped;
	}

	private void addSubmissionsToRequest(EventDataRequest request, DataRequestDetails requestDetails) {

		submissionRepo.findAllByRequest(request).get().findFirst().ifPresent(it -> addSubmissionToRequest(requestDetails, it));
	}

	private void addSubmissionToRequest(DataRequestDetails requestDetails, EventDataSubmission submission) {

		var dataProvider = modelMapper.map(submission.getDataProvider(), GuestListDataProvider.class);

		var guests = submission.getGuests().stream().map(it -> modelMapper.map(it, Guest.class)).collect(Collectors.toList());

		var guestList = GuestList.builder()
			.additionalInformation(submission.getAdditionalInformation())
			.startDate(submission.getStartDate())
			.endDate(submission.getEndDate())
			.dataProvider(dataProvider)
			.guests(guests)
			.build();

		requestDetails.setSubmissionData(guestList);
	}
}
