package iris.client_bff.events.message;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.iris_messages.data.*;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@Getter
@AllArgsConstructor
public class EventMessageDataProcessor implements IrisMessageDataProcessor {

    private final String discriminator = "event-tracking";

    private final EventDataRequestService requestService;
    private final EventDataSubmissionService submissionService;

    private final EventMessageDataConverter dataConverter;

    private final Validator validator;

    @Override
    public void validateInsert(String insert) throws ResponseStatusException {
        EventMessageDataInsertPayload insertPayload = EventMessageDataConverter.getInsertPayload(insert);
        Set<ConstraintViolation<EventMessageDataInsertPayload>> constraintViolations = validator.validate(insertPayload);
        if (!constraintViolations.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_INPUT + ": " + constraintViolations.stream().map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage())).collect(Collectors.joining(", "))
            );
    }

    @Override
    public String payloadFromInsert(String insert) throws IrisMessageDataException {
        return dataConverter.payloadFromInsert(insert);
    }

    @Override
    public String payloadFromTransfer(String transfer) throws IrisMessageDataException {
        return dataConverter.payloadFromTransfer(transfer);
    }

    // @todo: add validation / defuse!
    @Override
    public void importPayload(String payload) throws IrisMessageDataException {
        EventMessageDataPayload messagePayload = EventMessageDataPayload.toModel(payload);
        EventMessageDataPayload.EventDataRequestPayload requestPayload = messagePayload.getEventDataRequestPayload();
        EventDataRequest eventDataRequest = new EventDataRequest(
                requestPayload.getRefId(),
                requestPayload.getName(),
                requestPayload.getRequestStart(),
                requestPayload.getRequestEnd(),
                requestPayload.getComment(),
                requestPayload.getRequestDetails(),
                requestPayload.getHdUserId(),
                requestPayload.getLocation(),
                requestPayload.getAnnouncementToken()
        );
        EventDataRequest request = this.requestService.save(eventDataRequest);
        this.submissionService.save(request, messagePayload.getEventDataSubmissionPayload().getGuestList());
    }

    // @todo: add validation / defuse!
    @Override
    public DataRequestDetails viewPayload(String payload) throws IrisMessageDataException {
        EventMessageDataPayload messagePayload = EventMessageDataPayload.toModel(payload);
        EventMessageDataPayload.EventDataRequestPayload requestPayload = messagePayload.getEventDataRequestPayload();
        EventMessageDataPayload.EventDataSubmissionPayload submissionPayload = messagePayload.getEventDataSubmissionPayload();
        return DataRequestDetails.builder()
                .name(requestPayload.getName())
                .start(requestPayload.getRequestStart())
                .end(requestPayload.getRequestEnd())
                .requestDetails(requestPayload.getRequestDetails())
                .submissionData(submissionPayload.getGuestList())
                .comment(requestPayload.getComment())
                .build();
    }
}
