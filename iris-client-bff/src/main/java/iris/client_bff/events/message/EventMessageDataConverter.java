package iris.client_bff.events.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.iris_messages.IrisMessageTransfer;
import iris.client_bff.iris_messages.data.IrisMessageDataException;
import iris.client_bff.iris_messages.data.IrisMessageDataInsert;
import iris.client_bff.iris_messages.data.IrisMessageData;
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

    public IrisMessageData dataFromInsert(IrisMessageDataInsert irisMessageDataInsert) throws IrisMessageDataException {
        EventDataRequest.DataRequestIdentifier eventId = this.getEventId(irisMessageDataInsert);
        EventDataRequest eventDataRequest = this.getEventDataRequest(eventId);
        EventDataSubmission eventDataSubmission = this.getEventDataSubmission(eventId);
        EventMessageDataPayload payload = EventMessageDataPayload.fromModel(
                eventDataRequest,
                eventDataSubmission,
                this.getGuestIds(irisMessageDataInsert)
        );
        return new IrisMessageData()
                .setDescription(irisMessageDataInsert.getDescription())
                .setDiscriminator(irisMessageDataInsert.getDiscriminator())
                .setPayload(EventMessageDataPayload.toString(payload));
    }

    public IrisMessageData dataFromTransfer(IrisMessageTransfer.DataAttachment dataAttachment) throws IrisMessageDataException {
        return new IrisMessageData()
                .setDescription(dataAttachment.getDescription())
                .setDiscriminator(dataAttachment.getDiscriminator())
                .setPayload(dataAttachment.getPayload());
    }

    private EventDataRequest.DataRequestIdentifier getEventId(IrisMessageDataInsert irisMessageDataInsert) throws IrisMessageDataException {
        EventMessageDataPayloadInsert payloadInsert = this.getInsertPayload(irisMessageDataInsert);
        UUID id = payloadInsert.getId();
        return EventDataRequest.DataRequestIdentifier.of(id);
    }

    private List<UUID> getGuestIds(IrisMessageDataInsert irisMessageDataInsert) throws IrisMessageDataException {
        EventMessageDataPayloadInsert payloadInsert = this.getInsertPayload(irisMessageDataInsert);
        return payloadInsert.getGuests();
    }

    private EventMessageDataPayloadInsert getInsertPayload(IrisMessageDataInsert irisMessageDataInsert) throws IrisMessageDataException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(irisMessageDataInsert.getPayload(), EventMessageDataPayloadInsert.class);
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
