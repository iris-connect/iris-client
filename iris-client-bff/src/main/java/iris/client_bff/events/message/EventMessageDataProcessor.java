package iris.client_bff.events.message;

import iris.client_bff.core.web.dto.Person;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.message.dto.ImportSelectionDto;
import iris.client_bff.events.message.dto.ExportSelectionDto;
import iris.client_bff.events.message.dto.ImportSelectionViewPayloadDto;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Getter
@AllArgsConstructor
public class EventMessageDataProcessor implements IrisMessageDataProcessor {

	private final String discriminator = "event-tracking";

	private final EventDataRequestService requestService;
	private final EventDataSubmissionService submissionService;
	private final EventDataSubmissionRepository submissionRepository;

	private final EventMessageDataBuilder dataBuilder;

	private final EventMessageDataPayloadDefuse payloadDefuse;

	private final Validator validator;
	private final MessageSourceAccessor messages;

	@Override
	public void validateExportSelection(String exportSelection) throws IrisMessageDataException {
		ExportSelectionDto payload = ExportSelectionDto.toModel(exportSelection);
		this.validatePayload(payload);
	}

	@Override
	public void validateImportSelection(String importSelection) throws IrisMessageDataException {
		ImportSelectionDto payload = ImportSelectionDto.toModel(importSelection);
		this.validatePayload(payload);
	}

	@Override
	public String buildPayload(String exportSelection) throws IrisMessageDataException {
		EventMessageDataPayload payload = this.dataBuilder.buildPayload(exportSelection);
		return EventMessageDataPayload.toString(payload);
	}

	@Override
	public void importPayload(String payload) throws IrisMessageDataException {
		EventMessageDataPayload messagePayload = this.getDefusedPayload(payload);
		EventMessageDataPayload.EventDataRequestPayload requestPayload = messagePayload.getEventDataRequestPayload();
		EventDataRequest eventDataRequest = EventDataRequest.builder()
				.refId("-")
				.name(requestPayload.getName())
				.requestStart(requestPayload.getRequestStart())
				.requestEnd(requestPayload.getRequestEnd())
				.build();
		EventDataRequest request = this.requestService.save(eventDataRequest);
		this.submissionService.save(request, messagePayload.getEventDataSubmissionPayload().getGuestList());
	}

	@Override
	public void importPayload(String payload, UUID importTargetId, String selection) throws IrisMessageDataException {
		EventMessageDataPayload messagePayload = this.getDefusedPayload(payload);
		EventDataSubmission eventDataSubmission = this.getEventDataSubmission(importTargetId);
		ImportSelectionDto importSelection = ImportSelectionDto.toModel(selection);
		ModelMapper mapper = new ModelMapper();
		messagePayload.getEventDataSubmissionPayload().getGuestList().getGuests().stream()
				.filter(it -> importSelection.getGuests().contains(it.getMessageDataSelectId()))
				.map(it -> mapper.map(it, iris.client_bff.events.model.Guest.class))
				.map(it -> it.setSubmission(eventDataSubmission))
				.forEach(it -> eventDataSubmission.getGuests().add(it));
		this.submissionRepository.save(eventDataSubmission);
	}

	@Override
	public Object getViewPayload(String payload) throws IrisMessageDataException {
		EventMessageDataPayload messagePayload = this.getDefusedPayload(payload);
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
		EventMessageDataPayload messagePayload = this.getDefusedPayload(payload);
		EventMessageDataPayload.EventDataSubmissionPayload submissionPayload = messagePayload
				.getEventDataSubmissionPayload();
		List<Guest> guests = submissionPayload.getGuestList().getGuests();
		List<String> duplicateGuests = this.getDuplicateGuests(guests, importTargetId);
		return ImportSelectionViewPayloadDto.builder()
				.selectables(ImportSelectionViewPayloadDto.Selectables.builder().guests(guests).build())
				.duplicates(ImportSelectionViewPayloadDto.Duplicates.builder().guests(duplicateGuests).build())
				.build();
	}

	private List<String> getDuplicateGuests(List<Guest> guests, UUID importTargetId) {
		ModelMapper modelMapper = new ModelMapper();
		EventDataSubmission eventDataSubmission = this.getEventDataSubmission(importTargetId);
		var targetGuests = eventDataSubmission.getGuests();
		if (guests == null || targetGuests == null) {
			return null;
		}
		List<Person> targetPeople = targetGuests.stream()
				.map(guest -> modelMapper.map(guest, Person.class)).toList();
		modelMapper.createTypeMap(Guest.class, Person.class);
		return guests.stream().filter(guest -> {
			Person mapped = modelMapper.map(guest, Person.class);
			return targetPeople.contains(mapped);
		}).map(Guest::getMessageDataSelectId).toList();
	}

	private EventDataRequest getEventDataRequest(UUID requestId) {
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

	private EventDataSubmission getEventDataSubmission(UUID requestId) {
		return this.getEventDataSubmission(this.getEventDataRequest(requestId));
	}

	private EventMessageDataPayload getDefusedPayload(String payload) throws IrisMessageDataException {
		EventMessageDataPayload messagePayload = EventMessageDataPayload.toModel(payload);
		this.payloadDefuse.defuse(messagePayload);
		return messagePayload;
	}

	private <T> void validatePayload(T payload) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(payload);
		if (!constraintViolations.isEmpty())
			throw new IrisMessageDataException(ErrorMessages.INVALID_INPUT + ": "
					+ constraintViolations.stream().map(
							violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
							.collect(Collectors.joining(", ")));
	}

}
