package iris.client_bff.iris_messages;

import com.fasterxml.jackson.core.type.TypeReference;
import iris.client_bff.RestResponsePage;
import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IrisMessageServiceTest {

	TypeReference<RestResponsePage<IrisMessage>> PAGE_TYPE = new TypeReference<>() {};

	IrisMessageTestData testData;

	@Mock
    IrisMessageRepository messageRepository;

	@Mock
	IrisMessageFolderRepository folderRepository;

	@Mock
	IrisMessageFileRepository fileRepository;

	@Mock
	HibernateSearcher searcher;

	@Mock
	EPSIrisMessageClient irisMessageClient;

	@Mock
	IrisMessageBuilder irisMessageBuilder;

	IrisMessageService service;

	@BeforeEach
	void setUp() {
		this.testData = new IrisMessageTestData();
		this.service = new IrisMessageService(
				this.messageRepository,
				this.folderRepository,
				this.fileRepository,
				this.searcher,
				this.irisMessageClient,
				this.irisMessageBuilder
		);
	}

	@Test
	void findById() {
		when(this.messageRepository.findById(any())).thenReturn(Optional.of(testData.MOCK_INBOX_MESSAGE));

		var message = this.service.findById(this.testData.MOCK_INBOX_MESSAGE.getId().toUUID());

		verify(this.messageRepository).findById(any());

		assertTrue(message.isPresent());
		assertEquals(message.get(), this.testData.MOCK_INBOX_MESSAGE);
	}

	@Test
	void search() {

		var folderId = this.testData.MOCK_INBOX_FOLDER.getId();

		Page<IrisMessage> messagePage = new PageImpl<>(List.of(this.testData.MOCK_INBOX_MESSAGE));

		when(this.messageRepository.findAllByFolderIdOrderByIsReadAsc(eq(folderId), nullable(Pageable.class))).thenReturn(messagePage);

		var messages = this.service.search(folderId.toUUID(), null,null);

		verify(this.messageRepository).findAllByFolderIdOrderByIsReadAsc(eq(folderId), nullable(Pageable.class));

		assertEquals(1, messages.getContent().size());
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

		var folderId = this.testData.MOCK_INBOX_FOLDER.getId();

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
		// @todo
	}
}
