package iris.client_bff.iris_messages.eps;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolderRepository;
import iris.client_bff.iris_messages.IrisMessageRepository;
import iris.client_bff.iris_messages.IrisMessageTestData;
import iris.client_bff.ui.messages.ErrorMessages;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;

@IrisWebIntegrationTest
class IrisMessageDataControllerTest {

	@Autowired
	IrisMessageDataController dataController;

	@Autowired
	IrisMessageRepository messageRepository;

	@Autowired
	IrisMessageFolderRepository folderRepository;

	@Autowired
	EPSIrisMessageClient messageClient;

	@Autowired
	MessageSourceAccessor messages;

	@Test
	void createIrisMessage() {

		IrisMessage localMessage = this.messageRepository.save(this.getMessage());
		IrisMessageTransferDto localMessageTransfer = IrisMessageTransferDto.fromEntity(localMessage);

		IrisMessageTransferDto remoteMessageTransfer = this.dataController.createIrisMessage(localMessageTransfer);

		assertNotNull(remoteMessageTransfer);
		assertThat(localMessageTransfer).isEqualTo(remoteMessageTransfer);
	}

	@Test
	void createIrisMessage_shouldFail_noData() {

		var e = assertThrows(IrisMessageException.class, () -> this.dataController.createIrisMessage(null));

		assertNotNull(e.getMessage());
		assertThat(e.getMessage()).contains(messages.getMessage("iris_message.invalid_id"));
	}

	@Test
	void createIrisMessage_shouldFail_invalidData() {

		IrisMessageTransferDto localMessageTransfer = Mockito.spy(IrisMessageTransferDto.fromEntity(this.getMessage()));

		localMessageTransfer.setSubject(IrisMessageTestData.INVALID_SUBJECT);
		verify(localMessageTransfer).setSubject(IrisMessageTestData.INVALID_SUBJECT);

		localMessageTransfer.setBody(IrisMessageTestData.INVALID_BODY);
		verify(localMessageTransfer).setBody(IrisMessageTestData.INVALID_BODY);

		var e = assertThrows(IrisMessageException.class,
				() -> this.dataController.createIrisMessage(localMessageTransfer));

		assertNotNull(e.getMessage());
		assertTrue(e.getMessage().contains(ErrorMessages.INVALID_INPUT));
	}

	private IrisMessage getMessage() {

		IrisMessageTestData testData = new IrisMessageTestData();

		Optional<IrisMessageFolder> outboxFolder = this.folderRepository
				.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);

		assertThat(outboxFolder.isPresent()).isTrue();

		IrisMessage message = testData.getTestOutboxMessage(outboxFolder.get());

		// for testing purposes, we use ourselves as message recipient
		message.setHdRecipient(this.messageClient.getOwnIrisMessageHdContact());

		return message;
	}

}
