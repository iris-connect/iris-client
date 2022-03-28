package iris.client_bff.iris_messages.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageDataService;
import iris.client_bff.iris_messages.IrisMessageDataTestData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.RestResponsePage;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.IrisMessageTestData;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@IrisWebIntegrationTest
@RequiredArgsConstructor
class IrisMessageDataControllerTest {

	private final String baseUrl = "/iris-messages/data";

	TypeReference<RestResponsePage<IrisMessageListItemDto>> PAGE_TYPE = new TypeReference<>() {};

	private final MockMvc mockMvc;
	private final ObjectMapper om;

	private final IrisMessageTestData messageTestData;
	private final IrisMessageDataTestData messageDataTestData;

	@MockBean
	private IrisMessageDataService irisMessageDataService;

	@MockBean
	private IrisMessageDataViewProvider irisMessageDataViewProvider;

	@MockBean
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
		IrisMessageData messageData = messageTestData.MOCK_INBOX_MESSAGE.getDataAttachments().get(0);

		doNothing().when(irisMessageDataService).importMessageData(any(IrisMessageData.IrisMessageDataIdentifier.class));

		var result = mockMvc
				.perform(
						MockMvcRequestBuilders.post(baseUrl + "/" + messageData.getId() + "/import/add")
				)
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		assertThat(result.getResponse().getContentAsString()).isEmpty();

		verify(irisMessageDataService).importMessageData(any(IrisMessageData.IrisMessageDataIdentifier.class));
	}

	@Test
	@WithMockUser()
	void importMessageDataAndUpdate() throws Exception {
		IrisMessageData messageData = messageTestData.MOCK_INBOX_MESSAGE.getDataAttachments().get(0);

		doNothing().when(irisMessageDataService).importMessageData(
				any(IrisMessageData.IrisMessageDataIdentifier.class),
				any(UUID.class),
				anyString()
		);

		when(irisMessageDataProcessors.getProcessor(any(IrisMessageData.IrisMessageDataIdentifier.class))).thenReturn(irisMessageDataProcessor);
		doNothing().when(irisMessageDataProcessor).validateImportSelection(anyString());

		var result = mockMvc
				.perform(
						MockMvcRequestBuilders.post(baseUrl + "/" + messageData.getId() + "/import/update")
								.queryParam("importTargetId", UUID.randomUUID().toString())
								.content(om.writeValueAsString(messageDataTestData.getImportSelection()))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		assertThat(result.getResponse().getContentAsString()).isEmpty();

		verify(irisMessageDataService).importMessageData(
				any(IrisMessageData.IrisMessageDataIdentifier.class),
				any(UUID.class),
				anyString()
		);

		verify(irisMessageDataProcessors).getProcessor(any(IrisMessageData.IrisMessageDataIdentifier.class));
		verify(irisMessageDataProcessor).validateImportSelection(anyString());
	}

	@Test
	@WithMockUser()
	void getMessageDataImportSelectionViewData() throws Exception {
		var viewDataDto = messageDataTestData.getImportSelectionDataViewDataDto(messageTestData.MOCK_INBOX_MESSAGE);

		when(irisMessageDataViewProvider.getImportSelectionViewData(
				any(IrisMessageData.IrisMessageDataIdentifier.class),
				any(UUID.class)
				)).thenReturn(viewDataDto);

		var result = mockMvc
				.perform(
						MockMvcRequestBuilders.get(baseUrl + "/" + viewDataDto.getId() + "/import/select")
								.queryParam("importTargetId", UUID.randomUUID().toString())
				)
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		var resultViewDataDto = om.readValue(result.getResponse().getContentAsString(), IrisMessageDataViewDataDto.class);

		assertThat(viewDataDto).isEqualTo(resultViewDataDto);

		verify(irisMessageDataViewProvider).getImportSelectionViewData(any(IrisMessageData.IrisMessageDataIdentifier.class), any(UUID.class));
	}

	@Test
	@WithMockUser()
	void getMessageDataViewData() throws Exception {
		var viewDataDto = messageDataTestData.getDataViewDataDto(messageTestData.MOCK_INBOX_MESSAGE);

		when(irisMessageDataViewProvider.getViewData(any(IrisMessageData.IrisMessageDataIdentifier.class)))
				.thenReturn(viewDataDto);

		var result = mockMvc
				.perform(
						MockMvcRequestBuilders.get(baseUrl + "/" + viewDataDto.getId() + "/view")
								.queryParam("importTargetId", UUID.randomUUID().toString())
				)
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		var resultViewDataDto = om.readValue(result.getResponse().getContentAsString(), IrisMessageDataViewDataDto.class);

		assertThat(viewDataDto).isEqualTo(resultViewDataDto);

		verify(irisMessageDataViewProvider).getViewData(any(IrisMessageData.IrisMessageDataIdentifier.class));
	}

}
