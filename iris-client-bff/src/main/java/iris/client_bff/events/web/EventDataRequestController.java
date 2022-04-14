package iris.client_bff.events.web;

import static org.springframework.http.ResponseEntity.*;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.EventMapper;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.DataRequestClient;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.events.web.dto.EventUpdateDTO;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocation;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.users.entities.UserAccount;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

@RestController
@RequestMapping("/data-requests-client/events")
@AllArgsConstructor
public class EventDataRequestController {

	private final EventDataRequestService dataRequestService;
	private final EventDataSubmissionRepository submissionRepo;
	private EventMapper eventMapper;

	// Must be a lambda expression instead of a method reference, otherwise it will result in an NPE.
	private final Function<EventDataRequest, ExistingDataRequestClientWithLocation> eventMapperFunction = it -> eventMapper
			.toExistingDataRequestClientDto(it);

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createDataRequest(@RequestBody @Valid DataRequestClient request) {

		var result = dataRequestService.createDataRequest(request);

		return ok(mapDataRequestDetails(result));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Page<ExistingDataRequestClientWithLocation> getDataRequests(
			@RequestParam(required = false) Status status,
			@RequestParam(required = false) String search,
			Pageable pageable) {

		if (status != null && StringUtils.isNotEmpty(search)) {
			return dataRequestService.search(status, search, pageable).map(eventMapperFunction);
		}
		if (StringUtils.isNotEmpty(search)) {
			return dataRequestService.search(search, pageable).map(eventMapperFunction);
		}
		if (status != null) {
			return dataRequestService.findByStatus(status, pageable).map(eventMapperFunction);
		}
		return dataRequestService.findAll(pageable).map(eventMapperFunction);
	}

	@GetMapping("/{code}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DataRequestDetails> getDataRequestByCode(@PathVariable DataRequestIdentifier code) {

		var dataRequest = dataRequestService.findById(code);

		if (dataRequest.isPresent()) {

			DataRequestDetails requestDetails = mapDataRequestDetails(dataRequest.get());
			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PatchMapping("/{code}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DataRequestDetails> update(@PathVariable DataRequestIdentifier code,
			@RequestBody @Valid EventUpdateDTO patch) {

		var dataRequest = dataRequestService.findById(code);
		if (dataRequest.isPresent()) {
			EventDataRequest updated = dataRequestService.update(dataRequest.get(), patch);

			DataRequestDetails requestDetails = mapDataRequestDetails(updated);
			addSubmissionsToRequest(dataRequest.get(), requestDetails);

			return ResponseEntity.of(Optional.of(requestDetails));
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	private DataRequestDetails mapDataRequestDetails(EventDataRequest request) {
		return eventMapper.toDataRequestDetailsDto(request);
	}

	private void addSubmissionsToRequest(EventDataRequest request, DataRequestDetails requestDetails) {
		submissionRepo.findAllByRequest(request).get().findFirst()
				.ifPresent(it -> addSubmissionToRequest(requestDetails, it));
	}

	private void addSubmissionToRequest(DataRequestDetails requestDetails, EventDataSubmission submission) {

		var dataProvider = eventMapper.toGuestListDataProviderDto(submission.getDataProvider());

		var guests = submission.getGuests().stream()
				.map(eventMapper::toGuestDto)
				.toList();

		var guestList = GuestList.builder()
				.additionalInformation(submission.getAdditionalInformation())
				.startDate(submission.getStartDate())
				.endDate(submission.getEndDate())
				.dataProvider(dataProvider)
				.guests(guests)
				.build();

		requestDetails.setSubmissionData(guestList);
	}

	private String getFullName(UserAccount user) {
		return user.getFirstName() + " " + user.getLastName();
	}
}
