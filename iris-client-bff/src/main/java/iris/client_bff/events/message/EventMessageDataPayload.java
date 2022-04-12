package iris.client_bff.events.message;

import iris.client_bff.core.serialization.DefuseJsonString;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.events.web.dto.GuestListDataProvider;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;

@Data
public class EventMessageDataPayload {

	private EventDataRequestPayload eventDataRequestPayload;
	private EventDataSubmissionPayload eventDataSubmissionPayload;

	public static EventMessageDataPayload fromModel(EventDataRequest eventDataRequest,
			EventDataSubmission eventDataSubmission, List<String> guestIds) {
		return new EventMessageDataPayload()
				.setEventDataRequestPayload(EventDataRequestPayload.fromModel(eventDataRequest))
				.setEventDataSubmissionPayload(EventDataSubmissionPayload.fromModel(eventDataSubmission, guestIds));
	}

	@Data
	public static class EventDataRequestPayload {

		@DefuseJsonString(maxLength = 500)
		private String name;

		private Instant requestStart;
		private Instant requestEnd;

		public static EventDataRequestPayload fromModel(EventDataRequest eventDataRequest) {
			return new EventDataRequestPayload()
					.setName(eventDataRequest.getName())
					.setRequestStart(eventDataRequest.getRequestStart())
					.setRequestEnd(eventDataRequest.getRequestEnd());
		}
	}

	@Data
	public static class EventDataSubmissionPayload {

		private GuestList guestList;

		public static EventDataSubmissionPayload fromModel(EventDataSubmission eventDataSubmission,
				List<String> guestIds) {
			ModelMapper mapper = new ModelMapper();
			List<Guest> guests = eventDataSubmission.getGuests().stream()
					.filter((guest -> guestIds.isEmpty() || guestIds.contains(guest.getGuestId().toString())))
					.map(it -> {
						Guest mapped = mapper.map(it, Guest.class);
						mapped.setGuestId(null);
						mapped.setMessageDataSelectId(UUID.randomUUID().toString());
						return mapped;
					}).toList();
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
