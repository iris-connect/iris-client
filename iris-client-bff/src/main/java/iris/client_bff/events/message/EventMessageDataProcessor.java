package iris.client_bff.events.message;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.iris_messages.data.*;
import iris.client_bff.iris_messages.data.IrisMessageDataException;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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

    private final EventMessageDataConverter dataConverter;

    private final EventMessageDataPayloadDefuse payloadDefuse;

    private final Validator validator;
    private final MessageSourceAccessor messages;

    @Override
    public void validateInsert(String insert) throws ResponseStatusException {
        EventMessageDataInsertPayload payload = EventMessageDataInsertPayload.toModel(insert);
        this.validatePayload(payload);
    }

    @Override
    public String getPayloadFromInsert(String insert) throws IrisMessageDataException {
        EventMessageDataPayload payload = dataConverter.getPayloadFromInsert(insert);
        return EventMessageDataPayload.toString(payload);
    }

    @Override
    public void validateImportSelection(String importSelection) throws ResponseStatusException {
        EventMessageDataImportSelectionPayload payload = EventMessageDataImportSelectionPayload.toModel(importSelection);
        this.validatePayload(payload);
    }

    @Override
    public void importPayload(String payload) throws IrisMessageDataException {
        EventMessageDataPayload messagePayload = this.getDefusedPayload(payload);
        EventMessageDataPayload.EventDataRequestPayload requestPayload = messagePayload.getEventDataRequestPayload();
        EventDataRequest eventDataRequest = EventDataRequest.builder()
                .refId(requestPayload.getRefId())
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
        EventMessageDataImportSelectionPayload importSelection = EventMessageDataImportSelectionPayload.toModel(selection);
        ModelMapper mapper = new ModelMapper();
        messagePayload.getEventDataSubmissionPayload().getGuestList().getGuests()
                .stream()
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
        EventMessageDataPayload.EventDataSubmissionPayload submissionPayload = messagePayload.getEventDataSubmissionPayload();
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
        EventMessageDataPayload.EventDataSubmissionPayload submissionPayload = messagePayload.getEventDataSubmissionPayload();
        //@todo: add filter for already selected guests
        return submissionPayload.getGuestList().getGuests();
    }

    private EventDataRequest getEventDataRequest(UUID requestId) {
        Optional<EventDataRequest> eventDataRequest = this.requestService.findById(requestId);
        if (eventDataRequest.isEmpty()) {
            throw new IrisMessageDataException(messages.getMessage("iris_message.invalid_message_data_import_target"));
        }
        return eventDataRequest.get();
    }

    private EventDataSubmission getEventDataSubmission(EventDataRequest eventDataRequest) {
        Optional<EventDataSubmission> eventDataSubmission = this.submissionRepository.findAllByRequest(eventDataRequest).get().findFirst();
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_INPUT + ": " + constraintViolations.stream().map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage())).collect(Collectors.joining(", "))
            );
    }

}
