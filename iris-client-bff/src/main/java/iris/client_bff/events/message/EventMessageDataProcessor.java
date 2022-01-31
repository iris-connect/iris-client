package iris.client_bff.events.message;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.iris_messages.IrisMessageTransfer;
import iris.client_bff.iris_messages.data.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
@AllArgsConstructor
public class EventMessageDataProcessor implements IrisMessageDataProcessor {

    private final String discriminator = "event-tracking";

    private final EventDataRequestService requestService;
    private final EventDataSubmissionService submissionService;

    private final EventMessageDataConverter dataConverter;

    @Override
    public IrisMessageData convertToData(IrisMessageDataInsert irisMessageDataInsert) throws IrisMessageDataException {
        return dataConverter.dataFromInsert(irisMessageDataInsert);
    }

    @Override
    public IrisMessageData convertToData(IrisMessageTransfer.DataAttachment dataAttachment) throws IrisMessageDataException {
        return dataConverter.dataFromTransfer(dataAttachment);
    }

    // @todo: add validation / defuse!
    @Override
    public void importData(IrisMessageData irisMessageData) throws IrisMessageDataException {
        EventMessageDataPayload messagePayload = EventMessageDataPayload.toModel(irisMessageData.getPayload());
        EventMessageDataPayload.EventDataRequestPayload payload = messagePayload.getEventDataRequestPayload();
        EventDataRequest eventDataRequest = new EventDataRequest(
                payload.getRefId(),
                payload.getName(),
                payload.getRequestStart(),
                payload.getRequestEnd(),
                payload.getComment(),
                payload.getRequestDetails(),
                payload.getHdUserId(),
                payload.getLocation(),
                payload.getAnnouncementToken()
        );
        EventDataRequest request = this.requestService.save(eventDataRequest);
        this.submissionService.save(request, messagePayload.getEventDataSubmissionPayload().getGuestList());
    }

    // @todo: add validation / defuse!
    @Override
    public IrisMessageViewData viewData(IrisMessageData irisMessageData) throws IrisMessageDataException {
        EventMessageDataPayload messagePayload = EventMessageDataPayload.toModel(irisMessageData.getPayload());
        EventMessageDataPayload.EventDataRequestPayload requestPayload = messagePayload.getEventDataRequestPayload();
        EventMessageDataPayload.EventDataSubmissionPayload submissionPayload = messagePayload.getEventDataSubmissionPayload();
        DataRequestDetails details = DataRequestDetails.builder()
                .name(requestPayload.getName())
                .start(requestPayload.getRequestStart())
                .end(requestPayload.getRequestEnd())
                .requestDetails(requestPayload.getRequestDetails())
                .submissionData(submissionPayload.getGuestList())
                .comment(requestPayload.getComment())
                .build();
        return new IrisMessageViewData()
                .setId(irisMessageData.getId().toString())
                .setDiscriminator(irisMessageData.getDiscriminator())
                .setPayload(details);
    }
}
