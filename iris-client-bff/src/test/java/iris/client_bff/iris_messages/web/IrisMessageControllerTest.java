package iris.client_bff.iris_messages.web;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.RestResponsePage;
import iris.client_bff.events.web.TestData;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessage.IrisMessageIdentifier;
import iris.client_bff.iris_messages.IrisMessageBuilder;
import iris.client_bff.iris_messages.IrisMessageFile.IrisMessageFileIdentifier;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolder.IrisMessageFolderIdentifier;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.IrisMessageService;
import iris.client_bff.iris_messages.IrisMessageTestData;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@MockBean
	private IrisMessageBuilder irisMessageBuilder;

	@Test
	void endpointShouldBeProtected() throws Exception {
		mockMvc.perform(get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void getInboxMessages() throws Exception {
		this.getMessages(testData.MOCK_INBOX_MESSAGE, testData.MOCK_INBOX_MESSAGE.getFolder().getId());
	}

	@Test
	@WithMockUser()
	void getOutboxMessages() throws Exception {
		this.getMessages(testData.MOCK_OUTBOX_MESSAGE, testData.MOCK_OUTBOX_MESSAGE.getFolder().getId());
	}

	@Test
	@WithMockUser()
	void getMessages_shouldFail() throws Exception {
		mockMvc.perform(get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andReturn();
	}

	@Test
	@WithMockUser()
	public void createAndSendMessage() throws Exception {
		IrisMessage irisMessage = testData.MOCK_OUTBOX_MESSAGE;

		IrisMessageInsertDto messageInsert = this.testData.getTestMessageInsert(irisMessage);

		when(irisMessageBuilder.build(any(IrisMessageInsertDto.class))).thenReturn(irisMessage);

		when(irisMessageService.sendMessage(any())).thenReturn(irisMessage);
		when(irisMessageService.findById(irisMessage.getId())).thenReturn(Optional.of(irisMessage));

		ObjectMapper objectMapper = new ObjectMapper();

		var postResult = mockMvc
				.perform(
						MockMvcRequestBuilders.post(baseUrl)
								.content(objectMapper.writeValueAsString(messageInsert))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();

		verify(irisMessageBuilder).build(messageInsert);
		verify(irisMessageService).sendMessage(any());

		String location = postResult.getResponse().getHeader("location");
		assert location != null;
		String messageId = location.substring(location.lastIndexOf('/') + 1);

		var res = mockMvc.perform(get(baseUrl + "/" + messageId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).findById(any());

		var messageDetailsDto = om.readValue(res.getResponse().getContentAsString(), IrisMessageDetailsDto.class);

		assertThat(messageDetailsDto.getHdRecipient().getId()).isEqualTo(messageInsert.getHdRecipient());
		assertThat(messageDetailsDto.getSubject()).isEqualTo(messageInsert.getSubject());
		assertThat(messageDetailsDto.getBody()).isEqualTo(messageInsert.getBody());

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
								.param("body", IrisMessageTestData.INVALID_BODY))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andReturn();

	}

	@Test
	@WithMockUser()
	void getMessageDetails() throws Exception {

		IrisMessageIdentifier messageId = testData.MOCK_INBOX_MESSAGE.getId();

		when(irisMessageService.findById(messageId)).thenReturn(Optional.of(testData.MOCK_INBOX_MESSAGE));

		var res = mockMvc.perform(get(baseUrl + "/" + messageId))
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

		IrisMessageIdentifier invalidId = IrisMessageIdentifier.of(UUID.randomUUID());

		when(irisMessageService.findById(invalidId)).thenReturn(Optional.empty());

		var res = mockMvc.perform(get(baseUrl + "/" + invalidId))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andReturn();

		verify(irisMessageService).findById(invalidId);

		assertThat(res.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	@WithMockUser()
	void updateMessage() throws Exception {
		IrisMessageUpdateDto messageUpdate = new IrisMessageUpdateDto(true);

		IrisMessage updatedMessage = spy(testData.getTestInboxMessage());
		updatedMessage.setIsRead(true);
		verify(updatedMessage).setIsRead(true);

		when(irisMessageService.findById(any())).thenReturn(Optional.of(updatedMessage));
		when(irisMessageService.saveMessage(updatedMessage)).thenReturn(updatedMessage);

		var res = mockMvc
				.perform(
						MockMvcRequestBuilders.patch(baseUrl + "/" + updatedMessage.getId())
								.content(om.writeValueAsString(messageUpdate))
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).findById(any());
		verify(irisMessageService).saveMessage(any(IrisMessage.class));

		var messageDetailsDto = om.readValue(res.getResponse().getContentAsString(), IrisMessageDetailsDto.class);

		assertEquals(messageDetailsDto.getIsRead(), true);
		assertThat(messageDetailsDto).isEqualTo(IrisMessageDetailsDto.fromEntity(updatedMessage));
	}

	@Test
	@WithMockUser()
	void updateMessage_shouldFail() throws Exception {
		UUID invalidId = UUID.randomUUID();

		IrisMessageUpdateDto messageUpdate = new IrisMessageUpdateDto(true);

		when(irisMessageService.findById(any())).thenReturn(Optional.empty());

		mockMvc
				.perform(
						MockMvcRequestBuilders.patch(baseUrl + "/" + invalidId)
								.content(om.writeValueAsString(messageUpdate))
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void getMessageFolders() throws Exception {

		List<IrisMessageFolder> folderList = List.of(testData.MOCK_INBOX_FOLDER, testData.MOCK_OUTBOX_FOLDER);

		when(irisMessageService.getFolders()).thenReturn(folderList);

		var res = mockMvc.perform(get(baseUrl + "/folders"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getFolders();

		List<IrisMessageFolderDto> folderDtoList = om.readValue(res.getResponse().getContentAsString(),
				new TypeReference<>() {});

		assertEquals(2, folderDtoList.size());
		assertThat(folderDtoList).isEqualTo(IrisMessageFolderDto.fromEntity(folderList));
	}

	@Test
	@WithMockUser()
	void downloadMessageFile() throws Exception {
		when(irisMessageService.findFileById(any(IrisMessageFileIdentifier.class))).thenReturn(Optional.of(testData.MOCK_MESSAGE_FILE));

		var res = mockMvc
				.perform(MockMvcRequestBuilders.get(baseUrl + "/files/{id}/download", testData.MOCK_MESSAGE_FILE.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).findFileById(any(IrisMessageFileIdentifier.class));

		assertThat(res.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION)).contains(testData.MOCK_MESSAGE_FILE.getName());
		assertThat(res.getResponse().getHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)).isEqualTo(HttpHeaders.CONTENT_DISPOSITION);
	}

	@Test
	@WithMockUser()
	void getMessageHdContactsWithoutOwn() throws Exception {

		when(irisMessageService.getHdContacts(null)).thenReturn(List.of(testData.MOCK_CONTACT_OTHER));

		var res = mockMvc
				.perform(get(baseUrl + "/hd-contacts"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getHdContacts(null);

		List<IrisMessageHdContact> contacts = om.readValue(res.getResponse().getContentAsString(),
				new TypeReference<>() {});

		assertEquals(1, contacts.size());
		assertThat(contacts).contains(testData.MOCK_CONTACT_OTHER);
		assertThat(contacts).doesNotContain(testData.MOCK_CONTACT_OWN);
	}

	@Test
	@WithMockUser()
	void getMessageHdContactsIncludingOwn() throws Exception {

		when(irisMessageService.getHdContacts(null)).thenReturn(List.of(testData.MOCK_CONTACT_OTHER));
		when(irisMessageService.getOwnHdContact()).thenReturn(testData.MOCK_CONTACT_OWN);

		var res = mockMvc
				.perform(get(baseUrl + "/hd-contacts").queryParam("includeOwn", "true"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getHdContacts(null);
		verify(irisMessageService).getOwnHdContact();

		List<IrisMessageHdContact> contacts = om.readValue(res.getResponse().getContentAsString(),
				new TypeReference<>() {});

		assertEquals(2, contacts.size());
		assertThat(contacts).contains(testData.MOCK_CONTACT_OTHER);
		assertThat(contacts).contains(testData.MOCK_CONTACT_OWN);
	}

	@Test
	@WithMockUser()
	void getUnreadMessageCount() throws Exception {
		when(irisMessageService.getCountUnread()).thenReturn(2);

		var res = mockMvc
				.perform(get(baseUrl + "/count/unread"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getCountUnread();

		Integer count = om.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(2, count);
	}

	@Test
	@WithMockUser()
	void getUnreadMessageCountByFolder() throws Exception {
		when(irisMessageService.getCountUnreadByFolderId(any())).thenReturn(1);

		var res = mockMvc
				.perform(get(baseUrl + "/count/unread")
						.queryParam("folder", testData.MOCK_INBOX_FOLDER.getId().toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(irisMessageService).getCountUnreadByFolderId(testData.MOCK_INBOX_FOLDER.getId());

		Integer count = om.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(1, count);
	}

	private void getMessages(IrisMessage message, IrisMessageFolderIdentifier folderId) throws Exception {

		when(irisMessageService.search(eq(folderId), nullable(String.class), any(Pageable.class)))
				.thenReturn(new RestResponsePage<>(List.of(message)));

		var res = mockMvc.perform(get(baseUrl)
			.param("folder", folderId.toString()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		verify(irisMessageService).search(eq(folderId), nullable(String.class), any(Pageable.class));

		var messagesPage = om.readValue(res.getResponse().getContentAsString(), PAGE_TYPE);

		assertEquals(1, messagesPage.getContent().size());
		assertThat(messagesPage.getContent().get(0)).isEqualTo(IrisMessageListItemDto.fromEntity(message));

	}

}
