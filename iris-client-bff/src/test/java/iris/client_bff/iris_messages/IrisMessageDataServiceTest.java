package iris.client_bff.iris_messages;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.events.message.EventMessageTestData;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
public class IrisMessageDataServiceTest {

	EventMessageTestData eventMessageTestData;
	IrisMessageDataTestData testData;

	@Mock
	IrisMessageDataRepository messageDataRepository;

	@Mock
	IrisMessageDataProcessor messageDataProcessor;

	@Mock
	IrisMessageDataProcessors messageDataProcessors;

	IrisMessageDataService service;

	@BeforeEach
	void setUp() {
		this.eventMessageTestData = new EventMessageTestData();
		this.testData = new IrisMessageDataTestData();
		this.service = new IrisMessageDataService(
				this.messageDataRepository,
				this.messageDataProcessors);
	}

	@Test
	void getMessageData() {
		var messageData = this.testData.MOCK_MESSAGE_DATA;

		when(this.messageDataRepository.findById(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(Optional.of(messageData));

		var result = this.service.getMessageData(messageData.getId());

		verify(this.messageDataRepository).findById(messageData.getId());

		assertEquals(messageData, result);

	}

	@Test
	void importMessageDataAndAdd() {
		var messageData = this.testData.MOCK_MESSAGE_DATA;

		when(this.messageDataRepository.findById(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(Optional.of(messageData));

		when(this.messageDataRepository.save(any(IrisMessageData.class)))
				.thenReturn(messageData);

		when(this.messageDataProcessors.withProcessorFor(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(this.messageDataProcessor);

		doNothing().when(this.messageDataProcessor).importPayload(anyString());

		this.service.importMessageData(messageData.getId());

		verify(this.messageDataRepository).findById(messageData.getId());
		verify(this.messageDataRepository).save(messageData);
		verify(this.messageDataProcessors).withProcessorFor(messageData.getId());
		verify(this.messageDataProcessor).importPayload(messageData.getPayload());

		assertTrue(messageData.isImported());

	}

	@Test
	void importMessageDataAndUpdate() throws JsonProcessingException {
		var messageData = this.testData.MOCK_MESSAGE_DATA;

		var targetId = UUID.randomUUID();
		var selection = this.eventMessageTestData.MOCK_EVENT_MESSAGE_IMPORT_SELECTION_STRING;

		when(this.messageDataRepository.findById(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(Optional.of(messageData));

		when(this.messageDataRepository.save(any(IrisMessageData.class)))
				.thenReturn(messageData);

		when(this.messageDataProcessors.withProcessorFor(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(this.messageDataProcessor);

		doNothing().when(this.messageDataProcessor).importPayload(anyString(), any(UUID.class), anyString());

		this.service.importMessageData(messageData.getId(), targetId, selection);

		verify(this.messageDataRepository).findById(messageData.getId());
		verify(this.messageDataRepository).save(messageData);
		verify(this.messageDataProcessors).withProcessorFor(messageData.getId());
		verify(this.messageDataProcessor).importPayload(messageData.getPayload(), targetId, selection);

		assertTrue(messageData.isImported());

	}

}
