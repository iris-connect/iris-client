package iris.client_bff.iris_messages.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.IrisMessageFolderRepository;
import iris.client_bff.iris_messages.IrisMessageTestData;
import iris.client_bff.iris_messages.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IrisMessageBuilderWebTest {

	IrisMessageTestData testData;

	@Mock
	IrisMessageFolderRepository folderRepository;

	@Mock
	EPSIrisMessageClient irisMessageClient;

	@Mock
	IrisMessageDataProcessor irisMessageDataProcessor;

	@Mock
	IrisMessageDataProcessors irisMessageDataProcessors;

	IrisMessageBuilderWeb builder;

	@BeforeEach
	void setUp() {
		this.testData = new IrisMessageTestData();
		this.builder = new IrisMessageBuilderWeb(
				this.folderRepository,
				this.irisMessageClient,
				this.irisMessageDataProcessors
		);
	}

	@Test
	void buildInsert() {

		IrisMessage message = this.testData.MOCK_OUTBOX_MESSAGE;
		IrisMessageInsertDto messageInsert = this.testData.getTestMessageInsert(message);

		when(this.folderRepository.findFirstByContextAndParentFolderIsNull(any()))
				.thenReturn(Optional.of(this.testData.MOCK_OUTBOX_FOLDER));

		when(this.irisMessageClient.getOwnIrisMessageHdContact()).thenReturn(this.testData.MOCK_CONTACT_OWN);
		when(this.irisMessageClient.findIrisMessageHdContactById(any(String.class)))
				.thenReturn(Optional.of(this.testData.MOCK_CONTACT_OTHER));

		when(this.irisMessageDataProcessors.withProcessorFor(anyString())).thenReturn(this.irisMessageDataProcessor);
		when(this.irisMessageDataProcessor.buildPayload(messageInsert.getDataAttachments().get(0).getPayload()))
				.thenReturn(message.getDataAttachments().get(0).getPayload());

		var builtMessage = this.builder.build(messageInsert);

		verify(this.folderRepository).findFirstByContextAndParentFolderIsNull(any());

		verify(this.irisMessageClient).getOwnIrisMessageHdContact();
		verify(this.irisMessageClient).findIrisMessageHdContactById(any(String.class));

		verify(this.irisMessageDataProcessors).withProcessorFor(anyString());
		verify(this.irisMessageDataProcessor).buildPayload(anyString());

		// messages should be identical except ID: toString removes the ID
		assertEquals(message.toString(), builtMessage.toString());

	}

}
