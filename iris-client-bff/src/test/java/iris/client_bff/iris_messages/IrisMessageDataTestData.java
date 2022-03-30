package iris.client_bff.iris_messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import iris.client_bff.events.message.EventMessageTestData;
import iris.client_bff.iris_messages.web.IrisMessageDataViewDataDto;
import iris.client_bff.iris_messages.web.IrisMessageInsertDto;
import org.springframework.stereotype.Component;

@Component
public class IrisMessageDataTestData {

	// as an example, we use the event data for testing the message data attachment
	private final EventMessageTestData eventMessageTestData = new EventMessageTestData();

	public final IrisMessageData MOCK_MESSAGE_DATA = getMessageData(new IrisMessage());

	public final IrisMessageDataViewDataDto MOCK_IMPORT_SELECTION_VIEW_DATA = getImportSelectionDataViewDataDto();
	public final IrisMessageDataViewDataDto MOCK_DATA_VIEW_DATA = getDataViewDataDto();

	public final IrisMessageInsertDto.DataAttachment MOCK_EXPORT_DATA_ATTACHMENT = eventMessageTestData.MOCK_EVENT_MESSAGE_EXPORT_DATA_ATTACHMENT;

	public IrisMessageData getMessageData(IrisMessage message) {
		return this.eventMessageTestData.getMessageData(message);
	}

	private IrisMessageDataViewDataDto getImportSelectionDataViewDataDto() {
		IrisMessageData messageData = this.MOCK_MESSAGE_DATA;
		return new IrisMessageDataViewDataDto()
				.setId(messageData.getId().toString())
				.setDiscriminator(messageData.getDiscriminator())
				.setPayload(getPayloadPojo(this.eventMessageTestData.MOCK_EVENT_MESSAGE_IMPORT_SELECTION_VIEW_PAYLOAD));
	}

	private IrisMessageDataViewDataDto getDataViewDataDto() {
		IrisMessageData messageData = this.MOCK_MESSAGE_DATA;
		return new IrisMessageDataViewDataDto()
				.setId(messageData.getId().toString())
				.setDiscriminator(messageData.getDiscriminator())
				.setPayload(getPayloadPojo(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_VIEW_PAYLOAD));
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
