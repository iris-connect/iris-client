package iris.client_bff.events.message;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestsDataInitializer;
import iris.client_bff.events.EventDataSubmissionsDataInitializer;
import iris.client_bff.events.message.dto.ExportSelectionDto;
import iris.client_bff.events.message.dto.ImportSelectionDto;
import iris.client_bff.events.message.dto.ImportSelectionViewPayloadDto;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.web.IrisMessageInsertDto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class EventMessageTestData {

	public final EventDataRequest MOCK_EVENT_DATA_REQUEST = EventDataRequestsDataInitializer.DATA_REQUEST_1;
	public final EventDataSubmission MOCK_EVENT_DATA_SUBMISSION = EventDataSubmissionsDataInitializer
			.createDataSubmission();

	public final EventMessageDataPayload MOCK_EVENT_MESSAGE_DATA_PAYLOAD = getMessageDataPayload();
	public final String MOCK_EVENT_MESSAGE_DATA_PAYLOAD_STRING = stringifyJSON(MOCK_EVENT_MESSAGE_DATA_PAYLOAD);

	public final IrisMessageInsertDto.DataAttachment MOCK_EVENT_MESSAGE_EXPORT_DATA_ATTACHMENT = getExportDataAttachment();

	public final ExportSelectionDto MOCK_EVENT_MESSAGE_EXPORT_SELECTION = getExportSelection();
	public final String MOCK_EVENT_MESSAGE_EXPORT_SELECTION_STRING = stringifyJSON(MOCK_EVENT_MESSAGE_EXPORT_SELECTION);

	public final ImportSelectionDto MOCK_EVENT_MESSAGE_IMPORT_SELECTION = getImportSelection();
	public final String MOCK_EVENT_MESSAGE_IMPORT_SELECTION_STRING = stringifyJSON(MOCK_EVENT_MESSAGE_IMPORT_SELECTION);

	public final ImportSelectionViewPayloadDto MOCK_EVENT_MESSAGE_IMPORT_SELECTION_VIEW_PAYLOAD = getImportSelectionDataViewPayload();

	public final DataRequestDetails MOCK_EVENT_MESSAGE_DATA_VIEW_PAYLOAD = getDataViewPayload();

	public IrisMessageData getMessageData(IrisMessage message) {
		return new IrisMessageData()
				.setImported(false)
				.setMessage(message)
				.setDiscriminator("event-tracking")
				.setDescription("test description")
				.setPayload(stringifyJSON(this.getMessageDataPayload()));
	}

	private EventMessageDataPayload getMessageDataPayload() {
		return EventMessageDataPayload.fromModel(
				MOCK_EVENT_DATA_SUBMISSION.getRequest(),
				MOCK_EVENT_DATA_SUBMISSION,
				getExportSelection().getGuests());
	}

	private IrisMessageInsertDto.DataAttachment getExportDataAttachment() {
		return new IrisMessageInsertDto.DataAttachment()
				.setDiscriminator("event-tracking")
				.setDescription("test description")
				.setPayload(getPayloadPojo(this.getExportSelection()).toString());
	}

	private ExportSelectionDto getExportSelection() {
		List<String> guests = MOCK_EVENT_DATA_SUBMISSION.getGuests().stream().map((guest -> guest.getGuestId().toString()))
				.toList();
		return new ExportSelectionDto()
				.setEvent(MOCK_EVENT_DATA_SUBMISSION.getRequest().getId().toString())
				.setGuests(guests);
	}

	private ImportSelectionViewPayloadDto getImportSelectionDataViewPayload() {
		var guests = this.MOCK_EVENT_MESSAGE_DATA_PAYLOAD.getEventDataSubmissionPayload().getGuestList().getGuests();
		return ImportSelectionViewPayloadDto.builder()
				.selectables(ImportSelectionViewPayloadDto.Selectables.builder().guests(guests).build())
				.duplicates(ImportSelectionViewPayloadDto.Duplicates.builder()
						.guests(guests.stream().map(Guest::getMessageDataSelectId).toList()).build())
				.build();
	}

	private DataRequestDetails getDataViewPayload() {
		var requestPayload = this.MOCK_EVENT_MESSAGE_DATA_PAYLOAD.getEventDataRequestPayload();
		var submissionPayload = this.MOCK_EVENT_MESSAGE_DATA_PAYLOAD.getEventDataSubmissionPayload();
		return DataRequestDetails.builder()
				.name(requestPayload.getName())
				.start(requestPayload.getRequestStart())
				.end(requestPayload.getRequestEnd())
				.submissionData(submissionPayload.getGuestList())
				.build();
	}

	private ImportSelectionDto getImportSelection() {
		List<String> guests = MOCK_EVENT_DATA_SUBMISSION.getGuests().stream().map((guest -> guest.getGuestId().toString()))
				.toList();
		return new ImportSelectionDto()
				.setGuests(guests);
	}

	private <T> String stringifyJSON(T value) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			return objectMapper.writeValueAsString(value);
		} catch (Exception e) {
			return "";
		}
	}

	private Object getPayloadPojo(Object payload) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			String json = stringifyJSON(payload);
			return objectMapper.readValue(json, Object.class);
		} catch (JsonProcessingException e) {
			// ignored
		}
		return "";
	}
}
