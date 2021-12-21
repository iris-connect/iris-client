package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IrisMessageBuilderTest {

	IrisMessageTestData testData;

	@Mock
	IrisMessageFolderRepository folderRepository;

	@Mock
	EPSIrisMessageClient irisMessageClient;

	IrisMessageBuilder builder;

	@BeforeEach
	void setUp() {
		this.testData = new IrisMessageTestData();
		this.builder = new IrisMessageBuilder(this.folderRepository, this.irisMessageClient);
	}

	@Test
	void buildTransfer() {

		IrisMessage message = this.testData.MOCK_INBOX_MESSAGE;
		IrisMessageTransfer messageTransfer = IrisMessageTransfer.fromEntity(message);

		when(this.folderRepository.findFirstByContextAndParentFolderIsNull(any())).thenReturn(Optional.of(this.testData.MOCK_INBOX_FOLDER));

		when(this.irisMessageClient.getOwnIrisMessageHdContact()).thenReturn(this.testData.MOCK_CONTACT_OWN);

		var builtMessage = this.builder.build(messageTransfer);

		verify(this.folderRepository).findFirstByContextAndParentFolderIsNull(any());

		verify(this.irisMessageClient).getOwnIrisMessageHdContact();

		// messages should be identical except ID: toString removes the ID
		assertEquals(message.toString(), builtMessage.toString());

	}

	@Test
	void buildInsert() {

		IrisMessage message = this.testData.MOCK_OUTBOX_MESSAGE;
		IrisMessageInsert messageInsert = this.testData.getTestMessageInsert(message);

		when(this.folderRepository.findFirstByContextAndParentFolderIsNull(any())).thenReturn(Optional.of(this.testData.MOCK_OUTBOX_FOLDER));

		when(this.irisMessageClient.getOwnIrisMessageHdContact()).thenReturn(this.testData.MOCK_CONTACT_OWN);
		when(this.irisMessageClient.findIrisMessageHdContactById(any(String.class))).thenReturn(Optional.of(this.testData.MOCK_CONTACT_OTHER));

		var builtMessage = this.builder.build(messageInsert);

		verify(this.folderRepository).findFirstByContextAndParentFolderIsNull(any());

		verify(this.irisMessageClient).getOwnIrisMessageHdContact();
		verify(this.irisMessageClient).findIrisMessageHdContactById(any(String.class));

		// messages should be identical except ID: toString removes the ID
		assertEquals(message.toString(), builtMessage.toString());

	}

}
