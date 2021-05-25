package iris.client_bff.events.web;

import io.vavr.control.Option;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequestMapping("/data-requests-client/events")
@RestController
@AllArgsConstructor
public class EventDataRequestController {

	private EventDataRequestService dataRequestService;
	private EventDataSubmissionRepository submissionRepo;

	private ModelMapper modelMapper;

	private final Function<EventDataRequest, ExistingDataRequestClientWithLocation> eventMapperFunction = (
			EventDataRequest request) -> {
		ExistingDataRequestClientWithLocation mapped = EventMapper.map(request);
		mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
		return mapped;
	};

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
	public Page<ExistingDataRequestClientWithLocation> getDataRequests(
			@RequestParam(required = false) Status status,
			@RequestParam(required = false) String search, Pageable pageable) {
		if (status != null && StringUtils.isNotEmpty(search)) {
			return dataRequestService.findByStatusAndSearchByRefIdOrName(status, search, pageable)
					.map(eventMapperFunction);
		} else if (StringUtils.isNotEmpty(search)) {
			return dataRequestService.searchByRefIdOrName(search, pageable).map(eventMapperFunction);
		} else if (status != null) {
			return dataRequestService.findByStatus(status, pageable).map(eventMapperFunction);
		}
		return dataRequestService.findAll(pageable).map(eventMapperFunction);

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
