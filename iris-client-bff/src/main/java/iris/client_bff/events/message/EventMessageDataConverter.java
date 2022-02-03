package iris.client_bff.events.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.iris_messages.data.IrisMessageDataException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@Getter
@AllArgsConstructor
public class EventMessageDataConverter {

    private final EventDataRequestRepository requestRepository;
    private final EventDataSubmissionRepository submissionRepository;

    EventMessageDataPayload payloadFromInsert(String insert) throws IrisMessageDataException {
        EventMessageDataInsertPayload insertPayload = EventMessageDataConverter.getInsertPayload(insert);
        EventDataRequest.DataRequestIdentifier eventId = EventDataRequest.DataRequestIdentifier.of(insertPayload.getEvent());
        EventDataRequest eventDataRequest = this.getEventDataRequest(eventId);
        EventDataSubmission eventDataSubmission = this.getEventDataSubmission(eventId);
        return EventMessageDataPayload.fromModel(
                eventDataRequest,
                eventDataSubmission,
                insertPayload.getGuests()
        );
    }

    static EventMessageDataInsertPayload getInsertPayload(String payload) throws IrisMessageDataException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(payload, EventMessageDataInsertPayload.class);
        } catch (Throwable e) {
            throw new IrisMessageDataException("invalid payload");
        }
    }

    private EventDataRequest getEventDataRequest(EventDataRequest.DataRequestIdentifier eventId) throws IrisMessageDataException {
        Optional<EventDataRequest> optionalEventDataRequest = this.requestRepository.findById(eventId);
        if (optionalEventDataRequest.isEmpty()) {
            throw new IrisMessageDataException("missing data request");
        }
        return optionalEventDataRequest.get();
    }

    private EventDataSubmission getEventDataSubmission(EventDataRequest.DataRequestIdentifier eventId) throws IrisMessageDataException {
        EventDataRequest eventDataRequest = this.getEventDataRequest(eventId);
        Optional<EventDataSubmission> optionalEventDataSubmission = submissionRepository.findAllByRequest(eventDataRequest).get().findFirst();
        if (optionalEventDataSubmission.isEmpty()) {
            throw new IrisMessageDataException("missing data submission");
        }
        return optionalEventDataSubmission.get();
    }

}
