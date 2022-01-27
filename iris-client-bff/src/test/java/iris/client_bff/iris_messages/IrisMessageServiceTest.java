package iris.client_bff.iris_messages;

import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.hd_search.eps.EPSHdSearchClient;
import iris.client_bff.iris_messages.data.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.data.IrisMessageDataRepository;
import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IrisMessageServiceTest {

	IrisMessageTestData testData;

	@Mock
    IrisMessageRepository messageRepository;

	@Mock
	IrisMessageFolderRepository folderRepository;

	@Mock
	IrisMessageDataRepository dataRepository;

	@Mock
	IrisMessageFileRepository fileRepository;

	@Mock
	HibernateSearcher searcher;

	@Mock
	EPSIrisMessageClient irisMessageClient;

	@Mock
	EPSHdSearchClient hdSearchClient;

	@Mock
	IrisMessageBuilder irisMessageBuilder;

	@Mock
	IrisMessageDataProcessors messageDataProcessors;

	IrisMessageService service;

	private final UUID ID_NOT_FOUND = UUID.randomUUID();

	@BeforeEach
	void setUp() {
		this.testData = new IrisMessageTestData();
		this.service = new IrisMessageService(
				this.messageRepository,
				this.folderRepository,
				this.dataRepository,
				this.fileRepository,
				this.searcher,
				this.irisMessageClient,
				this.hdSearchClient,
				this.irisMessageBuilder,
				this.messageDataProcessors
		);
	}

	@Test
	void findById() {
		when(this.messageRepository.findById(any())).thenReturn(Optional.of(this.testData.MOCK_INBOX_MESSAGE));

		var message = this.service.findById(this.testData.MOCK_INBOX_MESSAGE.getId().toUUID());

		verify(this.messageRepository).findById(any());

		assertTrue(message.isPresent());
		assertEquals(message.get(), this.testData.MOCK_INBOX_MESSAGE);
	}

	@Test
	void findById_notFound() {
		when(this.messageRepository.findById(IrisMessage.IrisMessageIdentifier.of(this.ID_NOT_FOUND))).thenReturn(Optional.empty());

		var message = this.service.findById(this.ID_NOT_FOUND);

		verify(this.messageRepository).findById(any());

		assertTrue(message.isEmpty());
	}

	@Test
	void search() {

		IrisMessage message = this.testData.MOCK_INBOX_MESSAGE;

		var folderId = message.getFolder().getId();

		Page<IrisMessage> page = new PageImpl<>(List.of(message));

		when(this.messageRepository.findAllByFolderIdOrderByIsReadAsc(eq(folderId), nullable(Pageable.class))).thenReturn(page);

		var messagePage = this.service.search(folderId.toUUID(), null,null);

		verify(this.messageRepository).findAllByFolderIdOrderByIsReadAsc(eq(folderId), nullable(Pageable.class));

		assertEquals(1, messagePage.getContent().size());
		assertEquals(page.getContent(), messagePage.getContent());
	}

	@Test
	void getCountUnreadByFolderId() {

		var folderId = this.testData.MOCK_INBOX_FOLDER.getId();

		when(this.messageRepository.getCountUnreadByFolderId(any(IrisMessageFolder.IrisMessageFolderIdentifier.class))).thenReturn(3);

		var count = this.service.getCountUnreadByFolderId(folderId.toUUID());

		verify(this.messageRepository).getCountUnreadByFolderId(eq(folderId));

		assertEquals(3, count);

	}

	@Test
	void getCountUnread() {

		when(this.messageRepository.countByIsReadFalseOrIsReadIsNull()).thenReturn(3);

		var count = this.service.getCountUnread();

		verify(this.messageRepository).countByIsReadFalseOrIsReadIsNull();

		assertEquals(3, count);

	}

	@Test
	void getFolders() {

		when(this.folderRepository.findAll()).thenReturn(List.of(this.testData.MOCK_INBOX_FOLDER, this.testData.MOCK_OUTBOX_FOLDER));

		var folders = this.service.getFolders();

		verify(this.folderRepository).findAll();

		assertEquals(2, folders.size());

	}

	@Test
	void updateMessage() {

		IrisMessage message = this.testData.getTestInboxMessage();
		IrisMessageUpdate messageUpdate = new IrisMessageUpdate(true);

		when(this.messageRepository.save(any(IrisMessage.class))).then(AdditionalAnswers.returnsFirstArg());

		this.service.updateMessage(message, messageUpdate);

		verify(this.messageRepository).save(any(IrisMessage.class));

		assertEquals(message.getIsRead(), messageUpdate.getIsRead());

	}

	// disabled file attachments
	/*
	@Test
	void findFileById() {

		when(this.fileRepository.findById(any())).thenReturn(Optional.of(this.testData.MOCK_MESSAGE_FILE));

		var file = this.service.findFileById(this.testData.MOCK_MESSAGE_FILE.getId().toUUID());

		verify(this.fileRepository).findById(any());

		assertTrue(file.isPresent());
		assertEquals(file.get(), this.testData.MOCK_MESSAGE_FILE);

	}

	@Test
	void findFileById_notFound() {

		when(this.fileRepository.findById(IrisMessageFile.IrisMessageFileIdentifier.of(this.ID_NOT_FOUND))).thenReturn(Optional.empty());

		var file = this.service.findFileById(this.ID_NOT_FOUND);

		verify(this.fileRepository).findById(any());

		assertTrue(file.isEmpty());

	}
	 */

	@Test
	void getHdContacts() {

		when(this.irisMessageClient.getIrisMessageHdContacts()).thenReturn(List.of(this.testData.MOCK_CONTACT_OTHER));

		var contacts = this.service.getHdContacts(null);

		verify(this.irisMessageClient).getIrisMessageHdContacts();

		assertEquals(contacts.size(), 1);
		assertEquals(contacts.get(0), this.testData.MOCK_CONTACT_OTHER);

	}

	@Test
	void getOwnHdContact() {

		when(this.irisMessageClient.getOwnIrisMessageHdContact()).thenReturn(this.testData.MOCK_CONTACT_OWN);

		var contact = this.service.getOwnHdContact();

		verify(this.irisMessageClient).getOwnIrisMessageHdContact();

		assertEquals(contact, this.testData.MOCK_CONTACT_OWN);

	}

	@Test
	void sendMessage() {

		IrisMessage message = this.testData.MOCK_OUTBOX_MESSAGE;

		IrisMessageInsert messageInsert = this.testData.getTestMessageInsert(message);

		when(this.irisMessageBuilder.build(messageInsert)).thenReturn(message);
		doNothing().when(this.irisMessageClient).createIrisMessage(any(IrisMessage.class));
		when(this.messageRepository.save(any(IrisMessage.class))).then(AdditionalAnswers.returnsFirstArg());

		var sentMessage = this.service.sendMessage(messageInsert);

		verify(this.irisMessageBuilder).build(messageInsert);
		verify(this.irisMessageClient).createIrisMessage(message);
		verify(this.messageRepository).save(message);

		assertEquals(sentMessage, message);

	}

	@Test
	void receiveMessage() {

		IrisMessage message = this.testData.MOCK_INBOX_MESSAGE;

		IrisMessageTransfer messageTransfer = IrisMessageTransfer.fromEntity(message);

		when(this.irisMessageBuilder.build(messageTransfer)).thenReturn(message);
		when(this.messageRepository.save(any(IrisMessage.class))).then(AdditionalAnswers.returnsFirstArg());

		var receivedMessage = this.service.receiveMessage(messageTransfer);

		verify(this.irisMessageBuilder).build(messageTransfer);
		verify(this.messageRepository).save(message);

		assertEquals(receivedMessage, message);

	}

}
