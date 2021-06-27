package iris.client_bff.data_request.events.web;

import io.vavr.control.Option;
import iris.client_bff.data_request.events.EventDataRequest;
import iris.client_bff.data_request.events.EventDataRequestService;
import iris.client_bff.data_request.events.web.dto.DataRequestClient;
import iris.client_bff.data_request.events.web.dto.DataRequestDetails;
import iris.client_bff.data_request.events.web.dto.EventUpdateDTO;
import iris.client_bff.data_request.events.web.dto.ExistingDataRequestClientWithLocation;
import iris.client_bff.data_request.events.web.dto.ExistingDataRequestClientWithLocationList;
import iris.client_bff.data_request.events.web.dto.Guest;
import iris.client_bff.data_request.events.web.dto.GuestList;
import iris.client_bff.data_request.events.web.dto.GuestListDataProvider;
import iris.client_bff.data_request.events.web.dto.LocationInformation;
import iris.client_bff.data_submission.DataSubmissionRepository;
import iris.client_bff.data_submission.model.DataSubmission;
import lombok.AllArgsConstructor;

import java.util.Optional;
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

	private EventDataRequestService dataRequestService;
	private DataSubmissionRepository submissionRepo;

	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DataRequestDetails> createDataRequest(@Valid @RequestBody DataRequestClient request) {

		var result = dataRequestService.createLocationRequest(
				request.getExternalRequestId(),
				request.getName(),
				request.getStart(),
				request.getEnd(),
				Option.of(request.getComment()),
				Option.of(request.getRequestDetails()),
				request.getLocationId(),
				request.getProviderId());

		return ResponseEntity.ok(map(result));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ExistingDataRequestClientWithLocationList> getDataRequests() {
		var response = dataRequestService.getAll().stream().map(request -> {
			var mapped = modelMapper.map(request, ExistingDataRequestClientWithLocation.class);
			mapped.setCode(request.getId().toString());
			mapped.setStart(request.getRequestStart());
			mapped.setEnd(request.getRequestEnd());
			mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
			mapped.setLastUpdatedAt(request.getLastModifiedAt());
			mapped.setRequestedAt(request.getCreatedAt());
			mapped.setExternalRequestId(request.getRefId());
			return mapped;
		})
				.collect(Collectors.toList());
		var res = new ExistingDataRequestClientWithLocationList();
		res.setDataRequests(response);
		return ResponseEntity.of(Optional.of(res));
	}

	@GetMapping("/{code}")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<DataRequestDetails> getDataRequestByCode(@PathVariable String code) {
		var dataRequest = dataRequestService.findById(code);
		if (dataRequest.isPresent()) {

			var requestDetails = map(dataRequest.get());

			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{code}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DataRequestDetails> update(@PathVariable String code, @RequestBody
			EventUpdateDTO patch) {

		var dataRequest = dataRequestService.findById(code);
		if (dataRequest.isPresent()) {
			var updated = dataRequestService.update(dataRequest.get(), patch);
			var requestDetails = map(updated);

			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private DataRequestDetails map(EventDataRequest request) {
		var mapped = modelMapper.map(request, DataRequestDetails.class);
		mapped.setCode(request.getId().toString());
		mapped.setStart(request.getRequestStart());
		mapped.setEnd(request.getRequestEnd());
		mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
		mapped.setLastModifiedAt(request.getLastModifiedAt());
		mapped.setRequestedAt(request.getCreatedAt());
		mapped.setExternalRequestId(request.getRefId());
		return mapped;
	}

	/**
	 * @param request
	 * @param requestDetails
	 */
	private void addSubmissionsToRequest(EventDataRequest request, DataRequestDetails requestDetails) {

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

		var dataProvider = modelMapper.map(submission.getDataProvider(), GuestListDataProvider.class);

		var guests = submission.getGuests().stream()
				.map(it -> modelMapper.map(it, Guest.class))
				.collect(Collectors.toList());

		var guestList = new GuestList()
				.additionalInformation(submission.getAdditionalInformation())
				.startDate(submission.getStartDate())
				.endDate(submission.getEndDate())
				.dataProvider(dataProvider)
				.guests(guests);

		requestDetails.setSubmissionData(guestList);
	}
}