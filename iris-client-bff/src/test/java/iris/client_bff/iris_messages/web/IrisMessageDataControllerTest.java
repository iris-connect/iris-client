package iris.client_bff.iris_messages.web;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.events.message.EventMessageTestData;
import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.IrisMessageDataService;
import iris.client_bff.iris_messages.IrisMessageDataTestData;
import iris.client_bff.iris_messages.IrisMessageTestData;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@IrisWebIntegrationTest
@RequiredArgsConstructor
class IrisMessageDataControllerTest {

	private final String baseUrl = "/iris-messages/data";

	private final MockMvc mockMvc;
	private final ObjectMapper om;

	private final EventMessageTestData eventMessageTestData;
	private final IrisMessageTestData messageTestData;
	private final IrisMessageDataTestData messageDataTestData;

	@MockBean
	private IrisMessageDataService irisMessageDataService;

	@MockBean
	private IrisMessageDataViewProvider irisMessageDataViewProvider;

	@Mock
	IrisMessageDataProcessor irisMessageDataProcessor;

	@MockBean
	IrisMessageDataProcessors irisMessageDataProcessors;

	@Test
	void endpointShouldBeProtected() throws Exception {
		mockMvc.perform(get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void importMessageDataAndAdd() throws Exception {
		IrisMessageData messageData = spy(messageTestData.MOCK_INBOX_MESSAGE.getDataAttachments().get(0));

		doAnswer(invocation -> {
			messageData.setImported(true);
			return null;
		}).when(irisMessageDataService).importMessageData(any(IrisMessageData.IrisMessageDataIdentifier.class));

		var result = mockMvc
				.perform(
						MockMvcRequestBuilders.post(baseUrl + "/" + messageData.getId() + "/import"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		assertThat(result.getResponse().getContentAsString()).isEmpty();
		assertThat(messageData.isImported()).isTrue();

		verify(irisMessageDataService).importMessageData(messageData.getId());
		verify(messageData).setImported(true);
	}

	@Test
	@WithMockUser()
	void importMessageDataAndUpdate() throws Exception {
		IrisMessageData messageData = spy(messageTestData.MOCK_INBOX_MESSAGE.getDataAttachments().get(0));

		doAnswer(invocation -> {
			messageData.setImported(true);
			return null;
		}).when(irisMessageDataService).importMessageData(
				any(IrisMessageData.IrisMessageDataIdentifier.class),
				any(UUID.class),
				anyString());

		when(irisMessageDataProcessors.withProcessorFor(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(irisMessageDataProcessor);
		doNothing().when(irisMessageDataProcessor).validateImportSelection(anyString());

		var result = mockMvc
				.perform(
						MockMvcRequestBuilders.post(baseUrl + "/" + messageData.getId() + "/import")
								.queryParam("importTargetId", UUID.randomUUID().toString())
								.content(this.eventMessageTestData.MOCK_EVENT_MESSAGE_IMPORT_SELECTION_STRING)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		assertThat(result.getResponse().getContentAsString()).isEmpty();
		assertThat(messageData.isImported()).isTrue();

		verify(irisMessageDataService).importMessageData(
				any(IrisMessageData.IrisMessageDataIdentifier.class),
				any(UUID.class),
				anyString());

		verify(irisMessageDataProcessors).withProcessorFor(messageData.getId());
		verify(irisMessageDataProcessor).validateImportSelection(anyString());
		verify(messageData).setImported(true);
	}

	@Test
	@WithMockUser()
	void getMessageDataImportSelectionViewData() throws Exception {
		var viewDataDto = messageDataTestData.MOCK_IMPORT_SELECTION_VIEW_DATA;

		when(irisMessageDataViewProvider.getImportSelectionViewData(
				any(IrisMessageData.IrisMessageDataIdentifier.class),
				any(UUID.class))).thenReturn(viewDataDto);

		var result = mockMvc
				.perform(
						MockMvcRequestBuilders.get(baseUrl + "/" + viewDataDto.getId() + "/import-selection-view")
								.queryParam("importTargetId", UUID.randomUUID().toString()))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		var resultViewDataDto = om.readValue(result.getResponse().getContentAsString(), IrisMessageDataViewDataDto.class);

		assertThat(viewDataDto).isEqualTo(resultViewDataDto);

		verify(irisMessageDataViewProvider).getImportSelectionViewData(any(IrisMessageData.IrisMessageDataIdentifier.class),
				any(UUID.class));
	}

	@Test
	@WithMockUser()
	void getMessageDataViewData() throws Exception {
		var viewDataDto = messageDataTestData.MOCK_DATA_VIEW_DATA;

		when(irisMessageDataViewProvider.getViewData(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(viewDataDto);

		var result = mockMvc
				.perform(
						MockMvcRequestBuilders.get(baseUrl + "/" + viewDataDto.getId() + "/view")
								.queryParam("importTargetId", UUID.randomUUID().toString()))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		var resultViewDataDto = om.readValue(result.getResponse().getContentAsString(), IrisMessageDataViewDataDto.class);

		assertThat(viewDataDto).isEqualTo(resultViewDataDto);

		verify(irisMessageDataViewProvider).getViewData(any(IrisMessageData.IrisMessageDataIdentifier.class));
	}

}
