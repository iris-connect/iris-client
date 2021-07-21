package iris.client_bff.events.web;

import static org.springframework.http.ResponseEntity.ok;

import iris.client_bff.core.security.InputValidationUtility;
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

	private final InputValidationUtility inputValidationUtility;
	private EventDataRequestService dataRequestService;
	private EventDataSubmissionRepository submissionRepo;

	private ModelMapper modelMapper;

	private final Function<EventDataRequest, ExistingDataRequestClientWithLocation> eventMapperFunction = (EventDataRequest request) -> {
		ExistingDataRequestClientWithLocation mapped = EventMapper.map(request);
		mapped.setLocationInformation(modelMapper.map(request.getLocation(), LocationInformation.class));
		return mapped;
	};

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createDataRequest(@Valid @RequestBody DataRequestClient request) {

		DataRequestClient requestValidated = dataRequestClientInputValidated(request);

		var result = dataRequestService.createDataRequest(requestValidated);

		return ok(mapDataRequestDetails(result));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Page<ExistingDataRequestClientWithLocation> getDataRequests(
		@RequestParam(required = false) Status status,
		@RequestParam(required = false) String search,
		Pageable pageable) {
		if (isSearchInputValid(search)) {
			if (status != null && StringUtils.isNotEmpty(search)) {
				return dataRequestService.findByStatusAndSearchByRefIdOrName(status, search, pageable).map(eventMapperFunction);
			} else if (StringUtils.isNotEmpty(search)) {
				return dataRequestService.searchByRefIdOrName(search, pageable).map(eventMapperFunction);
			} else if (status != null) {
				return dataRequestService.findByStatus(status, pageable).map(eventMapperFunction);
			}
			return dataRequestService.findAll(pageable).map(eventMapperFunction);
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - search: " + search);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	@GetMapping("/{code}")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<DataRequestDetails> getDataRequestByCode(@PathVariable UUID code) {
		if (inputValidationUtility.isUUIDInputValid(code.toString())) {
			var dataRequest = dataRequestService.findById(code);
			if (dataRequest.isPresent()) {
				DataRequestDetails requestDetails = mapDataRequestDetails(dataRequest.get());
				addSubmissionsToRequest(dataRequest.get(), requestDetails);

				return ResponseEntity.of(Optional.of(requestDetails));
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - code: " + code.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping("/{code}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DataRequestDetails> update(@PathVariable UUID code, @RequestBody EventUpdateDTO patch) {
		if (inputValidationUtility.isUUIDInputValid(code.toString())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - code: " + code.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		EventUpdateDTO patchValidated = eventUpdateDTOInputValidated(patch);

		var dataRequest = dataRequestService.findById(code);
		if (dataRequest.isPresent()) {
			EventDataRequest updated = dataRequestService.update(dataRequest.get(), patchValidated);

			dataRequestService.sendDataRecievedEmail(updated, patchValidated.getStatus());

			DataRequestDetails requestDetails = mapDataRequestDetails(updated);
			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private EventUpdateDTO eventUpdateDTOInputValidated(EventUpdateDTO patch) {
		boolean isInvalid = false;

		if (patch == null) {
			isInvalid = true;
		}

		if (patch.getComment() != null) {
			if (!inputValidationUtility.checkInputForAttacks(patch.getComment())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - comment: " + patch.getComment());
				patch.setComment(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (patch.getName() != null) {
			if (!inputValidationUtility.checkInputForAttacks(patch.getName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - name: " + patch.getName());
				patch.setName(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (patch.getExternalRequestId() != null) {
			if (!inputValidationUtility.checkInputForAttacks(patch.getExternalRequestId())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - externalRequestId: " + patch.getExternalRequestId());
				patch.setExternalRequestId(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (!(patch.getStatus() == EventStatusDTO.DATA_RECEIVED
			|| patch.getStatus() == EventStatusDTO.DATA_REQUESTED
			|| patch.getStatus() == EventStatusDTO.ABORTED
			|| patch.getStatus() == EventStatusDTO.CLOSED)) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - status: " + patch.getStatus());
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return patch;
	}

	private DataRequestClient dataRequestClientInputValidated(DataRequestClient request) {
		boolean isInvalid = false;

		if (request == null) {
			isInvalid = true;
		}

		if (request.getComment() != null) {
			if (!inputValidationUtility.checkInputForAttacks(request.getComment())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - comment: " + request.getComment());
				request.setComment(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (request.getName() != null) {
			if (!inputValidationUtility.checkInputForAttacks(request.getName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - name: " + request.getName());
				request.setName(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (request.getRequestDetails() != null) {
			if (!inputValidationUtility.checkInputForAttacks(request.getRequestDetails())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - requestDetails: " + request.getRequestDetails());
				request.setRequestDetails(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (request.getExternalRequestId() == null || !inputValidationUtility.checkInputForAttacks(request.getExternalRequestId())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - externalRequestId: " + request.getExternalRequestId());
			request.setExternalRequestId(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (request.getProviderId() == null || !inputValidationUtility.checkInputForAttacks(request.getProviderId())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - providerId: " + request.getProviderId());
			request.setProviderId(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (request.getLocationId() == null || !inputValidationUtility.checkInputForAttacks(request.getLocationId())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - locationId: " + request.getLocationId());
			request.setLocationId(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (request.getStart() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - start: null");
			isInvalid = true;
		}

		if (request.getEnd() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - end: null");
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return request;
	}

	private boolean isSearchInputValid(String search) {
		if (search == null || inputValidationUtility.checkInputForAttacks(search)) {
			return false;
		}

		return true;
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
