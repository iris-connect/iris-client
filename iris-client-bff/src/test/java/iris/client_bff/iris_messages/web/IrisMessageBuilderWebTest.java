package iris.client_bff.iris_messages.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
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
import org.springframework.context.support.MessageSourceAccessor;

@ExtendWith(MockitoExtension.class)
public class IrisMessageBuilderWebTest {

	IrisMessageTestData testData;

	@Mock
	IrisMessageFolderRepository folderRepository;

	@Mock
	EPSIrisMessageClient irisMessageClient;

	@Mock
	IrisMessageDataProcessors irisMessageDataProcessors;

	@Mock
	ValidationHelper validationHelper;

	@Mock
	MessageSourceAccessor messages;

	IrisMessageBuilderWeb builder;

	@BeforeEach
	void setUp() {
		this.testData = new IrisMessageTestData();
		this.builder = new IrisMessageBuilderWeb(
				this.folderRepository,
				this.irisMessageClient,
				this.irisMessageDataProcessors,
				this.validationHelper
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

		var builtMessage = this.builder.build(messageInsert);

		verify(this.folderRepository).findFirstByContextAndParentFolderIsNull(any());

		verify(this.irisMessageClient).getOwnIrisMessageHdContact();
		verify(this.irisMessageClient).findIrisMessageHdContactById(any(String.class));

		// messages should be identical except ID: toString removes the ID
		assertEquals(message.toString(), builtMessage.toString());

	}

}
