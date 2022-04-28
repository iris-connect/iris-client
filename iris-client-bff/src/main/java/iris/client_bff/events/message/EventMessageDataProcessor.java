package iris.client_bff.events.message;

import iris.client_bff.core.web.dto.Person;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.EventMapper;
import iris.client_bff.events.message.dto.ExportSelectionDto;
import iris.client_bff.events.message.dto.ImportSelectionDto;
import iris.client_bff.events.message.dto.ImportSelectionViewPayloadDto;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import iris.client_bff.iris_messages.utils.IrisMessageDataUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
@Getter
@AllArgsConstructor
public class EventMessageDataProcessor implements IrisMessageDataProcessor {

	private final String discriminator = "event-tracking";

	private final EventDataRequestService requestService;
	private final EventDataSubmissionService submissionService;
	private final EventDataSubmissionRepository submissionRepository;

	private final EventMessageDataBuilder dataBuilder;

	private final IrisMessageDataUtils messageDataUtils;
	private final MessageSourceAccessor messages;

	private final EventMapper eventMapper;

	@Override
	public void validateExportSelection(String exportSelection) throws IrisMessageDataException {
		ExportSelectionDto payload = messageDataUtils.parseJSON(exportSelection, ExportSelectionDto.class);
		messageDataUtils.validatePayload(payload);
	}

	@Override
	public void validateImportSelection(String importSelection) throws IrisMessageDataException {
		ImportSelectionDto payload = messageDataUtils.parseJSON(importSelection, ImportSelectionDto.class);
		messageDataUtils.validatePayload(payload);
	}

	@Override
	public String buildPayload(String exportSelection) throws IrisMessageDataException {
		ExportSelectionDto exportSelectionDto = messageDataUtils.parseJSON(exportSelection, ExportSelectionDto.class);
		EventMessageDataPayload payload = this.dataBuilder.buildPayload(exportSelectionDto);
		return messageDataUtils.stringifyJSON(payload);
	}

	@Override
	public void importPayload(String payload) throws IrisMessageDataException {
		EventMessageDataPayload messagePayload = this.parsePayload(payload);
		EventMessageDataPayload.EventDataRequestPayload requestPayload = messagePayload.getEventDataRequestPayload();
		EventDataRequest eventDataRequest = EventDataRequest.builder()
				.refId(messages.getMessage("iris_message.message_data_substitution"))
				.name(requestPayload.getName())
				.requestStart(requestPayload.getRequestStart())
				.requestEnd(requestPayload.getRequestEnd())
				.build();
		EventDataRequest request = this.requestService.save(eventDataRequest);
		this.submissionService.save(request, messagePayload.getEventDataSubmissionPayload().getGuestList());
	}

	@Override
	public void importPayload(String payload, UUID importTargetId, String selection) throws IrisMessageDataException {
		EventMessageDataPayload messagePayload = this.parsePayload(payload);
		EventDataSubmission eventDataSubmission = this.getEventDataSubmission(DataRequestIdentifier.of(importTargetId));
		ImportSelectionDto importSelection = messageDataUtils.parseJSON(selection, ImportSelectionDto.class);

		messagePayload.getEventDataSubmissionPayload().getGuestList().getGuests().stream()
				.filter(it -> importSelection.getGuests().contains(it.getMessageDataSelectId()))
				.map(eventMapper::fromGuestDto)
				.map(it -> it.setSubmission(eventDataSubmission))
				.forEach(it -> eventDataSubmission.getGuests().add(it));
		this.submissionRepository.save(eventDataSubmission);
	}

	@Override
	public Object getViewPayload(String payload) throws IrisMessageDataException {
		EventMessageDataPayload messagePayload = this.parsePayload(payload);
		EventMessageDataPayload.EventDataRequestPayload requestPayload = messagePayload.getEventDataRequestPayload();
		EventMessageDataPayload.EventDataSubmissionPayload submissionPayload = messagePayload
				.getEventDataSubmissionPayload();
		return DataRequestDetails.builder()
				.name(requestPayload.getName())
				.start(requestPayload.getRequestStart())
				.end(requestPayload.getRequestEnd())
				.submissionData(submissionPayload.getGuestList())
				.build();
	}

	@Override
	public Object getImportSelectionViewPayload(String payload, UUID importTargetId) throws IrisMessageDataException {
		EventMessageDataPayload messagePayload = this.parsePayload(payload);
		EventMessageDataPayload.EventDataSubmissionPayload submissionPayload = messagePayload
				.getEventDataSubmissionPayload();
		List<Guest> guests = submissionPayload.getGuestList().getGuests();
		List<String> duplicateGuests = this.getDuplicateGuests(guests, DataRequestIdentifier.of(importTargetId));
		return ImportSelectionViewPayloadDto.builder()
				.selectables(ImportSelectionViewPayloadDto.Selectables.builder().guests(guests).build())
				.duplicates(ImportSelectionViewPayloadDto.Duplicates.builder().guests(duplicateGuests).build())
				.build();
	}

	private List<String> getDuplicateGuests(List<Guest> guests, DataRequestIdentifier importTargetId) {

		EventDataSubmission eventDataSubmission = this.getEventDataSubmission(importTargetId);
		var targetGuests = eventDataSubmission.getGuests();
		if (guests == null || targetGuests == null) {
			return null;
		}

		List<Person> targetPeople = targetGuests.stream().map(eventMapper::toPersonDto).toList();

		return guests.stream().filter(guest -> {
			Person mapped = eventMapper.toPersonDto(guest);
			return targetPeople.contains(mapped);
		}).map(Guest::getMessageDataSelectId).toList();
	}

	private EventDataRequest getEventDataRequest(DataRequestIdentifier requestId) {
		Optional<EventDataRequest> eventDataRequest = this.requestService.findById(requestId);
		if (eventDataRequest.isEmpty()) {
			throw new IrisMessageDataException(messages.getMessage("iris_message.invalid_message_data_import_target"));
		}
		return eventDataRequest.get();
	}

	private EventDataSubmission getEventDataSubmission(EventDataRequest eventDataRequest) {
		Optional<EventDataSubmission> eventDataSubmission = this.submissionRepository.findAllByRequest(eventDataRequest)
				.get().findFirst();
		if (eventDataSubmission.isEmpty()) {
			throw new IrisMessageDataException(messages.getMessage("iris_message.invalid_message_data_import_target"));
		}
		return eventDataSubmission.get();
	}

	private EventDataSubmission getEventDataSubmission(DataRequestIdentifier requestId) {
		return this.getEventDataSubmission(this.getEventDataRequest(requestId));
	}

	private EventMessageDataPayload parsePayload(String payload) throws IrisMessageDataException {
		return messageDataUtils.parseJSON(payload, EventMessageDataPayload.class);
	}

}
