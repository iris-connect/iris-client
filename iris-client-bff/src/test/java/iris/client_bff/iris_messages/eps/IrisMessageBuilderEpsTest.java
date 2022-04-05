package iris.client_bff.iris_messages.eps;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageFolderRepository;
import iris.client_bff.iris_messages.IrisMessageTestData;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

@ExtendWith(MockitoExtension.class)
public class IrisMessageBuilderEpsTest {

	IrisMessageTestData testData;

	@Mock
	IrisMessageFolderRepository folderRepository;

	@Mock
	EPSIrisMessageClient irisMessageClient;

	@Mock
	MessageSourceAccessor messages;

	@Mock
	ValidationHelper validationHelper;

	IrisMessageBuilderEps builder;

	@BeforeEach
	void setUp() {
		this.testData = new IrisMessageTestData();
		this.builder = new IrisMessageBuilderEps(
				this.folderRepository,
				this.irisMessageClient,
				this.messages,
				this.validationHelper
		);
	}

	@Test
	void buildTransfer() {

		IrisMessage message = this.testData.MOCK_INBOX_MESSAGE;
		IrisMessageTransferDto messageTransfer = IrisMessageTransferDto.fromEntity(message);

		when(this.folderRepository.findFirstByContextAndParentFolderIsNull(any()))
				.thenReturn(Optional.of(this.testData.MOCK_INBOX_FOLDER));

		when(this.irisMessageClient.getOwnIrisMessageHdContact()).thenReturn(this.testData.MOCK_CONTACT_OWN);

		var builtMessage = this.builder.build(messageTransfer);

		verify(this.folderRepository).findFirstByContextAndParentFolderIsNull(any());

		verify(this.irisMessageClient).getOwnIrisMessageHdContact();

		// messages should be identical except ID: toString removes the ID
		assertEquals(message.toString(), builtMessage.toString());

	}
}
