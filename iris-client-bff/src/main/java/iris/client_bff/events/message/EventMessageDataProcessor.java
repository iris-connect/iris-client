package iris.client_bff.events.message;

import iris.client_bff.core.utils.ValidationHelper;
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

    private final EventMessageDataPayloadDefuse payloadDefuse;

    private final Validator validator;

    @Override
    public void validateInsert(String insert) throws ResponseStatusException {
        // convert insert String to insert PayloadObject
        EventMessageDataInsertPayload insertPayload = EventMessageDataConverter.getInsertPayload(insert);
        // validate insert PayloadObject
        Set<ConstraintViolation<EventMessageDataInsertPayload>> constraintViolations = validator.validate(insertPayload);
        if (!constraintViolations.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_INPUT + ": " + constraintViolations.stream().map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage())).collect(Collectors.joining(", "))
            );
        // check if insert PayloadObject contains a possible attack
        this.validateUUID(insertPayload.getEvent(), "payload.eventId", ErrorMessages.INVALID_IRIS_MESSAGE_DATA);
        for ( String guestId : insertPayload.getGuests() ) {
            this.validateUUID(guestId, "payload.guestId", ErrorMessages.INVALID_IRIS_MESSAGE_DATA);
        }
    }

    @Override
    public String payloadFromInsert(String insert) throws IrisMessageDataException {
        EventMessageDataPayload payload = dataConverter.payloadFromInsert(insert);
        return EventMessageDataPayload.toString(payload);
    }

    @Override
    public String payloadFromTransfer(String transfer) throws IrisMessageDataException {
        // we do not defuse the payload when receiving it to be able to receive yet unknown payload types.
        // We defuse it while importing / viewing
        return transfer;
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

    private void validateUUID(String value, String field, String errorMessage) {
        if (value == null || !ValidationHelper.isUUIDInputValid(value, field)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage + ": " + field);
        }
    }

    private EventMessageDataPayload getDefusedPayload(String payload) throws IrisMessageDataException {
        EventMessageDataPayload messagePayload = EventMessageDataPayload.toModel(payload);
        this.payloadDefuse.defuse(messagePayload);
        return messagePayload;
    }

}
