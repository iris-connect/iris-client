package iris.client_bff.events.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import iris.client_bff.iris_messages.data.IrisMessageDataException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Data
public class EventMessageDataPayload {

    private EventDataRequestPayload eventDataRequestPayload;
    private EventDataSubmissionPayload eventDataSubmissionPayload;

    public static EventMessageDataPayload fromModel(
            EventDataRequest eventDataRequest,
            EventDataSubmission eventDataSubmission,
            List<UUID> guestIds
    ) {
        return new EventMessageDataPayload()
                .setEventDataRequestPayload(EventDataRequestPayload.fromModel(eventDataRequest))
                .setEventDataSubmissionPayload(EventDataSubmissionPayload.fromModel(eventDataSubmission, guestIds));
    }

    public static String toString(EventMessageDataPayload payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(payload);
        } catch (Throwable e) {
            throw new IrisMessageDataException(e);
        }
    }

    public static EventMessageDataPayload toModel(String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(payload, EventMessageDataPayload.class);
        } catch (Throwable e) {
            throw new IrisMessageDataException(e);
        }
    }

    @Data
    static class EventDataRequestPayload {

        private String refId;
        private String name;
        private Instant requestStart;
        private Instant requestEnd;
        private final String hdUserId = null;
        private String comment;
        private String requestDetails = null;
        private Location location = null;
        private final String announcementToken = null;

        public static EventDataRequestPayload fromModel(EventDataRequest eventDataRequest) {
            return new EventDataRequestPayload()
                    .setRefId(eventDataRequest.getRefId())
                    .setName(eventDataRequest.getName())
                    .setRequestStart(eventDataRequest.getRequestStart())
                    .setRequestEnd(eventDataRequest.getRequestEnd())
                    .setComment(eventDataRequest.getComment())
//                    .setLocation(eventDataRequest.getLocation())
                    ;
        }
    }

    @Data
    static class EventDataSubmissionPayload {

        private GuestList guestList;

        public static EventDataSubmissionPayload fromModel(EventDataSubmission eventDataSubmission, List<UUID> guestIds) {
            ModelMapper mapper = new ModelMapper();
            List<Guest> guests = eventDataSubmission.getGuests()
                    .stream()
                    .filter((guest -> guestIds.contains(guest.getGuestId())))
                    .map(it -> mapper.map(it, Guest.class))
                    .collect(Collectors.toList());
            GuestList guestList = GuestList.builder()
                    .guests(guests)
                    .dataProvider(mapper.map(eventDataSubmission.getDataProvider(), GuestListDataProvider.class))
                    .additionalInformation(eventDataSubmission.getAdditionalInformation())
                    .startDate(eventDataSubmission.getStartDate())
                    .endDate(eventDataSubmission.getEndDate())
                    .build();
            return new EventDataSubmissionPayload().setGuestList(guestList);
        }
    }
}
