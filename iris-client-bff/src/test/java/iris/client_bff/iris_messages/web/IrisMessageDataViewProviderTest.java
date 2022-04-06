package iris.client_bff.iris_messages.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.IrisMessageDataService;
import iris.client_bff.iris_messages.IrisMessageDataTestData;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IrisMessageDataViewProviderTest {

	IrisMessageDataTestData testData;

	@Mock
	IrisMessageDataService messageDataService;

	@Mock
	IrisMessageDataProcessor messageDataProcessor;

	@Mock
	IrisMessageDataProcessors messageDataProcessors;

	IrisMessageDataViewProvider messageDataViewProvider;

	@BeforeEach
	void setUp() {
		this.testData = new IrisMessageDataTestData();
		this.messageDataViewProvider = new IrisMessageDataViewProvider(
				this.messageDataService,
				this.messageDataProcessors);
	}

	@Test
	void getViewData() {

		var messageData = this.testData.MOCK_MESSAGE_DATA;
		var viewDataDto = this.testData.MOCK_DATA_VIEW_DATA;

		when(this.messageDataService.getMessageData(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(messageData);

		when(this.messageDataProcessors.withProcessorFor(anyString())).thenReturn(this.messageDataProcessor);
		when(this.messageDataProcessor.getViewPayload(messageData.getPayload()))
				.thenReturn(viewDataDto.getPayload());

		var result = this.messageDataViewProvider.getViewData(messageData.getId());

		verify(this.messageDataService).getMessageData(messageData.getId());

		verify(this.messageDataProcessors).withProcessorFor(anyString());
		verify(this.messageDataProcessor).getViewPayload(messageData.getPayload());

		assertEquals(viewDataDto, result);

	}

	@Test
	void getImportSelectionViewData() {

		var messageData = this.testData.MOCK_MESSAGE_DATA;
		var viewDataDto = this.testData.MOCK_IMPORT_SELECTION_VIEW_DATA;
		var targetId = UUID.randomUUID();

		when(this.messageDataService.getMessageData(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(messageData);

		when(this.messageDataProcessors.withProcessorFor(anyString())).thenReturn(this.messageDataProcessor);
		when(this.messageDataProcessor.getImportSelectionViewPayload(messageData.getPayload(), targetId))
				.thenReturn(viewDataDto.getPayload());

		var result = this.messageDataViewProvider.getImportSelectionViewData(messageData.getId(), targetId);

		verify(this.messageDataService).getMessageData(messageData.getId());

		verify(this.messageDataProcessors).withProcessorFor(anyString());
		verify(this.messageDataProcessor).getImportSelectionViewPayload(messageData.getPayload(), targetId);

		assertEquals(viewDataDto, result);

	}

}
