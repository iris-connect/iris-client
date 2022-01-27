package iris.client_bff.events.web;

import static org.springframework.http.ResponseEntity.*;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.DataRequestClient;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.events.web.dto.EventStatusDTO;
import iris.client_bff.events.web.dto.EventUpdateDTO;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocation;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import iris.client_bff.events.web.dto.LocationInformation;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequestMapping("/data-requests-client/events")
@RestController
@AllArgsConstructor
public class EventDataRequestController {

	private static final String FIELD_PROVIDER_ID = "providerId";
	private static final String FIELD_LOCATION_ID = "locationId";
	private static final String FIELD_REQUEST_DETAILS = "requestDetails";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_EXTERNAL_REQUEST_ID = "externalRequestId";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_COMMENT = "comment";
	private static final String FIELD_CODE = "code";
	private static final String FIELD_SEARCH = "search";

	private EventDataRequestService dataRequestService;
	private EventDataSubmissionRepository submissionRepo;

	private ValidationHelper validHelper;
	private ModelMapper modelMapper;

	private final Function<EventDataRequest, ExistingDataRequestClientWithLocation> eventMapperFunction = (
			EventDataRequest request) -> {
		ExistingDataRequestClientWithLocation mapped = EventMapper.map(request);
		if (request.getLocation() != null) {
			mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
		}
		return mapped;
	};

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createDataRequest(@Valid @RequestBody DataRequestClient request) {

		DataRequestClient requestValidated = validateDataRequestClient(request);

		var result = dataRequestService.createDataRequest(requestValidated);

		return ok(mapDataRequestDetails(result));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Page<ExistingDataRequestClientWithLocation> getDataRequests(
			@RequestParam(required = false) Status status,
			@RequestParam(required = false) String search,
			Pageable pageable) {
		if (validHelper.isPossibleAttack(search, FIELD_SEARCH, false)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (status != null && StringUtils.isNotEmpty(search)) {
			return dataRequestService.search(status, search, pageable).map(eventMapperFunction);
		} else if (StringUtils.isNotEmpty(search)) {
			return dataRequestService.search(search, pageable).map(eventMapperFunction);
		} else if (status != null) {
			return dataRequestService.findByStatus(status, pageable).map(eventMapperFunction);
		}
		return dataRequestService.findAll(pageable).map(eventMapperFunction);
	}

	@GetMapping("/{code}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DataRequestDetails> getDataRequestByCode(@PathVariable UUID code) {
		if (ValidationHelper.isUUIDInputValid(code.toString(), FIELD_CODE)) {
			var dataRequest = dataRequestService.findById(code);
			if (dataRequest.isPresent()) {
				DataRequestDetails requestDetails = mapDataRequestDetails(dataRequest.get());
				addSubmissionsToRequest(dataRequest.get(), requestDetails);

				return ResponseEntity.of(Optional.of(requestDetails));
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping("/{code}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DataRequestDetails> update(@PathVariable UUID code, @RequestBody EventUpdateDTO patch) {
		if (!ValidationHelper.isUUIDInputValid(code.toString(), FIELD_CODE)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		EventUpdateDTO patchValidated = validateEventUpdateDTO(patch);

		var dataRequest = dataRequestService.findById(code);
		if (dataRequest.isPresent()) {
			EventDataRequest updated = dataRequestService.update(dataRequest.get(), patchValidated);

			DataRequestDetails requestDetails = mapDataRequestDetails(updated);
			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private EventUpdateDTO validateEventUpdateDTO(EventUpdateDTO patch) {
		if (patch == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (validHelper.isPossibleAttack(patch.getComment(), FIELD_COMMENT, false)) {
			patch.setComment(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validHelper.isPossibleAttack(patch.getName(), FIELD_NAME, false)) {
			patch.setName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validHelper.isPossibleAttack(patch.getExternalRequestId(), FIELD_EXTERNAL_REQUEST_ID, false)) {
			patch.setExternalRequestId(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (patch.getStatus() != null
				&& !(patch.getStatus() == EventStatusDTO.DATA_RECEIVED
						|| patch.getStatus() == EventStatusDTO.DATA_REQUESTED
						|| patch.getStatus() == EventStatusDTO.ABORTED
						|| patch.getStatus() == EventStatusDTO.CLOSED)) {
			log.warn(ErrorMessages.INVALID_INPUT + FIELD_STATUS + patch.getStatus());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		return patch;
	}

	private DataRequestClient validateDataRequestClient(DataRequestClient request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (validHelper.isPossibleAttack(request.getComment(), FIELD_COMMENT, false)) {
			request.setComment(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validHelper.isPossibleAttack(request.getName(), FIELD_NAME, false)) {
			request.setName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validHelper.isPossibleAttack(request.getRequestDetails(), FIELD_REQUEST_DETAILS, false)) {
			request.setRequestDetails(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validHelper.isPossibleAttackForRequiredValue(request.getExternalRequestId(), FIELD_EXTERNAL_REQUEST_ID,
				false)) {
			request.setExternalRequestId(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validHelper.isPossibleAttackForRequiredValue(request.getProviderId(), FIELD_PROVIDER_ID, false)) {
			request.setProviderId(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validHelper.isPossibleAttackForRequiredValue(request.getLocationId(), FIELD_LOCATION_ID, false)) {
			request.setLocationId(ErrorMessages.INVALID_INPUT_STRING);
		}

		boolean isInvalid = false;

		if (request.getStart() == null) {
			log.warn(ErrorMessages.INVALID_INPUT + " - start: null");
			isInvalid = true;
		}

		if (request.getEnd() == null) {
			log.warn(ErrorMessages.INVALID_INPUT + " - end: null");
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		return request;
	}

	private DataRequestDetails mapDataRequestDetails(EventDataRequest request) {
		DataRequestDetails mapped = modelMapper.map(request, DataRequestDetails.class);
		mapped.setCode(request.getId().toString());
		mapped.setStart(request.getRequestStart());
		mapped.setEnd(request.getRequestEnd());
		if (request.getLocation() != null) {
			mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
		}
		mapped.setLastModifiedAt(request.getLastModifiedAt());
		mapped.setRequestedAt(request.getCreatedAt());
		mapped.setExternalRequestId(request.getRefId());
		return mapped;
	}

	private void addSubmissionsToRequest(EventDataRequest request, DataRequestDetails requestDetails) {
		submissionRepo.findAllByRequest(request).get().findFirst()
				.ifPresent(it -> addSubmissionToRequest(requestDetails, it));
	}

	private void addSubmissionToRequest(DataRequestDetails requestDetails, EventDataSubmission submission) {

		var dataProvider = modelMapper.map(submission.getDataProvider(), GuestListDataProvider.class);

		var guests = submission.getGuests().stream().map(it -> modelMapper.map(it, Guest.class))
				.collect(Collectors.toList());

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
