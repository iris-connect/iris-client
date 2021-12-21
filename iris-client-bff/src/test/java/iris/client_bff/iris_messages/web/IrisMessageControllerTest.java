package iris.client_bff.iris_messages.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.RestResponsePage;
import iris.client_bff.iris_messages.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IrisWebIntegrationTest
@RequiredArgsConstructor
class IrisMessageControllerTest {

	private final String baseUrl = "/iris-messages";

	TypeReference<RestResponsePage<IrisMessageListItemDto>> PAGE_TYPE = new TypeReference<>() {};

	private final MockMvc mockMvc;
	private final ObjectMapper om;

	private final IrisMessageTestData testData;

	@MockBean
	private IrisMessageService irisMessageService;

	@Test
	void endpointShouldBeProtected() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void getInboxMessages() throws Exception {
		this.getMessages(testData.MOCK_INBOX_MESSAGE, testData.MOCK_INBOX_MESSAGE.getFolder().getId().toUUID());
	}

	@Test
	@WithMockUser()
	void getOutboxMessages() throws Exception {
		this.getMessages(testData.MOCK_OUTBOX_MESSAGE, testData.MOCK_OUTBOX_MESSAGE.getFolder().getId().toUUID());
	}

	@Test
	@WithMockUser()
	void getMessages_shouldFail() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andReturn();
	}

	@Test
	@WithMockUser()
	public void createMessage() throws Exception {
		IrisMessage irisMessage = testData.MOCK_OUTBOX_MESSAGE;

		when(irisMessageService.sendMessage(any())).thenReturn(irisMessage);
		when(irisMessageService.findById(irisMessage.getId().toUUID())).thenReturn(Optional.of(irisMessage));

		var postResult = mockMvc
				.perform(
						MockMvcRequestBuilders
								.multipart(baseUrl)
								.param("hdRecipient", irisMessage.getHdRecipient().getId())
								.param("subject", irisMessage.getSubject())
								.param("body", irisMessage.getBody())
				)
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();

		verify(irisMessageService).sendMessage(any());

		String location = postResult.getResponse().getHeader("location");
		assert location != null;
		String messageId = location.substring(location.lastIndexOf('/') + 1);

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + messageId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).findById(any());

		var messageDetailsDto = om.readValue(res.getResponse().getContentAsString(), IrisMessageDetailsDto.class);

		assertThat(messageDetailsDto.getHdRecipient()).isEqualTo(irisMessage.getHdRecipient());
		assertThat(messageDetailsDto.getSubject()).isEqualTo(irisMessage.getSubject());
		assertThat(messageDetailsDto.getBody()).isEqualTo(irisMessage.getBody());

	}

	@Test
	@WithMockUser()
	public void createMessage_shouldFail() throws Exception {
		IrisMessage irisMessage = testData.MOCK_OUTBOX_MESSAGE;
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.multipart(baseUrl)
								.param("hdRecipient", irisMessage.getHdRecipient().getId())
								.param("subject", IrisMessageTestData.INVALID_SUBJECT)
								.param("body", IrisMessageTestData.INVALID_BODY)
				)
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andReturn();

	}

	@Test
	@WithMockUser()
	void getMessageDetails() throws Exception {

		UUID messageId = testData.MOCK_INBOX_MESSAGE.getId().toUUID();

		when(irisMessageService.findById(messageId)).thenReturn(Optional.of(testData.MOCK_INBOX_MESSAGE));

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + messageId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).findById(messageId);

		var messageDetailsDto = om.readValue(res.getResponse().getContentAsString(), IrisMessageDetailsDto.class);

		assertThat(messageDetailsDto)
				.isEqualTo(IrisMessageDetailsDto.fromEntity(testData.MOCK_INBOX_MESSAGE));
	}

	@Test
	@WithMockUser()
	void getMessageDetails_shouldFail() throws Exception {

		UUID invalidId = UUID.randomUUID();

		when(irisMessageService.findById(invalidId)).thenReturn(Optional.empty());

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + invalidId))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andReturn();

		verify(irisMessageService).findById(invalidId);

		assertThat(res.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	@WithMockUser()
	void updateMessage() throws Exception {
		IrisMessageUpdate messageUpdate = new IrisMessageUpdate(true);

		IrisMessage updatedMessage = spy(testData.getTestInboxMessage());
		updatedMessage.setIsRead(true);
		verify(updatedMessage).setIsRead(true);

		when(irisMessageService.findById(any(UUID.class))).thenReturn(Optional.of(updatedMessage));
		when(irisMessageService.updateMessage(updatedMessage, messageUpdate)).thenReturn(updatedMessage);

		var res = mockMvc
				.perform(
						MockMvcRequestBuilders.patch(baseUrl + "/" + updatedMessage.getId())
								.content(om.writeValueAsString(messageUpdate))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).findById(any(UUID.class));
		verify(irisMessageService).updateMessage(any(IrisMessage.class), any(IrisMessageUpdate.class));

		var messageDetailsDto = om.readValue(res.getResponse().getContentAsString(), IrisMessageDetailsDto.class);

		assertEquals(messageDetailsDto.getIsRead(), true);
		assertThat(messageDetailsDto).isEqualTo(IrisMessageDetailsDto.fromEntity(updatedMessage));
	}

	@Test
	@WithMockUser()
	void updateMessage_shouldFail() throws Exception {
		UUID invalidId = UUID.randomUUID();

		IrisMessageUpdate messageUpdate = new IrisMessageUpdate(true);

		when(irisMessageService.findById(any(UUID.class))).thenReturn(Optional.empty());

		mockMvc
				.perform(
						MockMvcRequestBuilders.patch(baseUrl + "/" + invalidId)
								.content(om.writeValueAsString(messageUpdate))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void getMessageFolders() throws Exception {

		List<IrisMessageFolder> folderList = List.of(testData.MOCK_INBOX_FOLDER, testData.MOCK_OUTBOX_FOLDER);

		when(irisMessageService.getFolders()).thenReturn(folderList);

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/folders"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getFolders();

		List<IrisMessageFolderDto> folderDtoList = om.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(2, folderDtoList.size());
		assertThat(folderDtoList).isEqualTo(IrisMessageFolderDto.fromEntity(folderList));
	}

	@Test
	@WithMockUser()
	void downloadMessageFile() throws Exception {
		when(irisMessageService.findFileById(any(UUID.class))).thenReturn(Optional.of(testData.MOCK_MESSAGE_FILE));

		var res = mockMvc
				.perform(MockMvcRequestBuilders.get(baseUrl + "/files/{id}/download", testData.MOCK_MESSAGE_FILE.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).findFileById(any(UUID.class));

		assertThat(res.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION)).contains(testData.MOCK_MESSAGE_FILE.getName());
		assertThat(res.getResponse().getHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)).isEqualTo(HttpHeaders.CONTENT_DISPOSITION);
	}

	@Test
	@WithMockUser()
	void getMessageHdContactsWithoutOwn() throws Exception {

		when(irisMessageService.getHdContacts()).thenReturn(List.of(testData.MOCK_CONTACT_OTHER));

		var res = mockMvc
				.perform(MockMvcRequestBuilders.get(baseUrl + "/hd-contacts"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getHdContacts();

		List<IrisMessageHdContact> contacts = om.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(1, contacts.size());
		assertThat(contacts).contains(testData.MOCK_CONTACT_OTHER);
		assertThat(contacts).doesNotContain(testData.MOCK_CONTACT_OWN);
	}

	@Test
	@WithMockUser()
	void getMessageHdContactsIncludingOwn() throws Exception {

		when(irisMessageService.getHdContacts()).thenReturn(List.of(testData.MOCK_CONTACT_OTHER));
		when(irisMessageService.getOwnHdContact()).thenReturn(testData.MOCK_CONTACT_OWN);

		var res = mockMvc
				.perform(MockMvcRequestBuilders.get(baseUrl + "/hd-contacts").queryParam("includeOwn", "true"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getHdContacts();
		verify(irisMessageService).getOwnHdContact();

		List<IrisMessageHdContact> contacts = om.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(2, contacts.size());
		assertThat(contacts).contains(testData.MOCK_CONTACT_OTHER);
		assertThat(contacts).contains(testData.MOCK_CONTACT_OWN);
	}

	@Test
	@WithMockUser()
	void getUnreadMessageCount() throws Exception {
		when(irisMessageService.getCountUnread()).thenReturn(2);

		var res = mockMvc
				.perform(MockMvcRequestBuilders.get(baseUrl + "/count/unread"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getCountUnread();

		Integer count = om.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(2, count);
	}

	@Test
	@WithMockUser()
	void getUnreadMessageCountByFolder() throws Exception {
		when(irisMessageService.getCountUnreadByFolderId(any(UUID.class))).thenReturn(1);

		var res = mockMvc
				.perform(MockMvcRequestBuilders
						.get(baseUrl + "/count/unread")
						.queryParam("folder", testData.MOCK_INBOX_FOLDER.getId().toString())
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getCountUnreadByFolderId(testData.MOCK_INBOX_FOLDER.getId().toUUID());

		Integer count = om.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(1, count);
	}

	private void getMessages(IrisMessage message, UUID folderId) throws Exception {

		when(irisMessageService.search(eq(folderId), nullable(String.class), any(Pageable.class)))
				.thenReturn(new RestResponsePage<>(List.of(message)));

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl).param("folder", folderId.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).search(eq(folderId), nullable(String.class), any(Pageable.class));

		var messagesPage = om.readValue(res.getResponse().getContentAsString(), PAGE_TYPE);

		assertEquals(1, messagesPage.getContent().size());
		assertThat(messagesPage.getContent().get(0)).isEqualTo(IrisMessageListItemDto.fromEntity(message));

	}

}
