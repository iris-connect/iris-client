package iris.client_bff.iris_messages.eps;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.iris_messages.*;
import iris.client_bff.iris_messages.web.IrisMessageController;
import iris.client_bff.ui.messages.ErrorMessages;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@IrisWebIntegrationTest
class IrisMessageDataControllerTest {

	@Autowired
	IrisMessageController controller;

	@Autowired
	IrisMessageDataController dataController;

	@Autowired
	IrisMessageRepository messageRepository;

	@Autowired
	IrisMessageFolderRepository folderRepository;

	@Autowired
	EPSIrisMessageClient messageClient;

	@Test
	void createIrisMessage() {

		IrisMessage localMessage = this.messageRepository.save(this.getMessage());
		IrisMessageTransfer localMessageTransfer = IrisMessageTransfer.fromEntity(localMessage);

		IrisMessageTransfer remoteMessageTransfer = this.dataController.createIrisMessage(localMessageTransfer);

		assertNotNull(remoteMessageTransfer);
		assertThat(localMessageTransfer).isEqualTo(remoteMessageTransfer);
	}

	@Test
	void createIrisMessage_shouldFail_noData() {

		var e = assertThrows(ResponseStatusException.class, () -> this.dataController.createIrisMessage(null));

		assertNotNull(e.getMessage());
		assertTrue(e.getMessage().contains(ErrorMessages.IRIS_MESSAGE_SUBMISSION));
	}

	@Test
	void createIrisMessage_shouldFail_invalidData() {

		IrisMessageTransfer localMessageTransfer = Mockito.spy(IrisMessageTransfer.fromEntity(this.getMessage()));

		localMessageTransfer.setSubject(IrisMessageTestData.INVALID_SUBJECT);
		verify(localMessageTransfer).setSubject(IrisMessageTestData.INVALID_SUBJECT);

		localMessageTransfer.setBody(IrisMessageTestData.INVALID_BODY);
		verify(localMessageTransfer).setBody(IrisMessageTestData.INVALID_BODY);

		var e = assertThrows(ResponseStatusException.class, () -> this.dataController.createIrisMessage(localMessageTransfer));

		assertNotNull(e.getMessage());
		assertTrue(e.getMessage().contains(ErrorMessages.INVALID_INPUT));
	}

	private IrisMessage getMessage() {

		IrisMessageTestData testData = new IrisMessageTestData();

		Optional<IrisMessageFolder> outboxFolder = this.folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);

		assertThat(outboxFolder.isPresent()).isTrue();

		IrisMessage message = testData.getTestOutboxMessage(outboxFolder.get());

		// @todo: remove next line as soon as dummy loopback functionality is removed / EPS message endpoints are implemented
		message.setHdRecipient(this.messageClient.getOwnIrisMessageHdContact());

		return message;
	}

}
