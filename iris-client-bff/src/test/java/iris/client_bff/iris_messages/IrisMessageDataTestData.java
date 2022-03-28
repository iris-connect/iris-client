package iris.client_bff.iris_messages;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import iris.client_bff.events.EventDataSubmissionsDataInitializer;

import iris.client_bff.events.message.EventMessageDataPayload;
import iris.client_bff.events.message.dto.ExportSelectionDto;
import iris.client_bff.events.message.dto.ImportSelectionDto;
import iris.client_bff.events.message.dto.ImportSelectionViewPayloadDto;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.DataRequestDetails;
import iris.client_bff.events.web.dto.Guest;
import iris.client_bff.iris_messages.web.IrisMessageDataViewDataDto;
import iris.client_bff.iris_messages.web.IrisMessageInsertDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IrisMessageDataTestData {

	private final EventDataSubmission MOCK_EVENT_DATA_SUBMISSION = EventDataSubmissionsDataInitializer.createDataSubmission();

	public IrisMessageData getMessageData(IrisMessage message) {
		return new IrisMessageData()
				.setImported(false)
				.setMessage(message)
				.setDiscriminator("event-tracking")
				.setDescription("test description")
				.setPayload(stringifyJSON(getMessageDataPayload()));
	}

	public IrisMessageInsertDto.DataAttachment getExportDataAttachment() {
		return new IrisMessageInsertDto.DataAttachment()
				.setDiscriminator("event-tracking")
				.setDescription("test description")
				.setPayload(getPayloadPojo(getExportSelection()).toString());
	}

	private EventMessageDataPayload getMessageDataPayload() {
		return EventMessageDataPayload.fromModel(
				MOCK_EVENT_DATA_SUBMISSION.getRequest(),
				MOCK_EVENT_DATA_SUBMISSION,
				getExportSelection().getGuests()
		);
	}

	private ExportSelectionDto getExportSelection() {
		List<String> guests = MOCK_EVENT_DATA_SUBMISSION.getGuests().stream().map((guest -> guest.getGuestId().toString())).toList();
		return new ExportSelectionDto()
				.setEvent(MOCK_EVENT_DATA_SUBMISSION.getRequest().getId().toString())
				.setGuests(guests);
	}

	public ImportSelectionDto getImportSelection() {
		List<String> guests = MOCK_EVENT_DATA_SUBMISSION.getGuests().stream().map((guest -> guest.getGuestId().toString())).toList();
		return new ImportSelectionDto()
				.setGuests(guests);
	}

	public IrisMessageDataViewDataDto getImportSelectionDataViewDataDto(IrisMessage message) {
		IrisMessageData messageData = getMessageData(message);
		var payload = parseJSON(messageData.getPayload(), EventMessageDataPayload.class);
		assert payload != null;
		var guests = payload.getEventDataSubmissionPayload().getGuestList().getGuests();
		ImportSelectionViewPayloadDto viewPayloadDto = ImportSelectionViewPayloadDto.builder()
				.selectables(ImportSelectionViewPayloadDto.Selectables.builder().guests(guests).build())
				.duplicates(ImportSelectionViewPayloadDto.Duplicates.builder().guests(guests.stream().map(Guest::getMessageDataSelectId).toList()).build())
				.build();
		return new IrisMessageDataViewDataDto()
				.setId(messageData.getId().toString())
				.setDiscriminator(messageData.getDiscriminator())
				.setPayload(getPayloadPojo(viewPayloadDto));
	}

	public IrisMessageDataViewDataDto getDataViewDataDto(IrisMessage message) {
		IrisMessageData messageData = getMessageData(message);
		var payload = parseJSON(messageData.getPayload(), EventMessageDataPayload.class);
		assert payload != null;
		var requestPayload = payload.getEventDataRequestPayload();
		var submissionPayload = payload.getEventDataSubmissionPayload();
		DataRequestDetails details = DataRequestDetails.builder()
				.name(requestPayload.getName())
				.start(requestPayload.getRequestStart())
				.end(requestPayload.getRequestEnd())
				.submissionData(submissionPayload.getGuestList())
				.build();
		return new IrisMessageDataViewDataDto()
				.setId(messageData.getId().toString())
				.setDiscriminator(messageData.getDiscriminator())
				.setPayload(getPayloadPojo(details));
	}

	private <T> T parseJSON(String value, Class<T> valueType) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.readValue(value, valueType);
		} catch (Exception e) {
			return null;
		}
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

	public Object getPayloadPojo(Object payload) {
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
