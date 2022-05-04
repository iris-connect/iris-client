package iris.client_bff.iris_messages;

import static java.time.Duration.*;
import static org.assertj.core.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.IrisDateTimeProvider;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class IrisMessageDeleteJobIntegrationTests {

	private final IrisMessageRepository messages;
	private final IrisMessageDataRepository data;
	private final IrisMessageFolderRepository folders;
	private final IrisDateTimeProvider dateTimeProvider;
	private final IrisMessageDeleteJob deleteJob;

	@Test
	void testDeleteMessages() {

		var messagesSize = messages.findAll().size();
		var dataSize = data.findAll().size();

		// in time
		dateTimeProvider.setDelta(ofDays(-179));

		createMessage();

		// to old
		dateTimeProvider.setDelta(ofDays(-181));

		createMessage();

		dateTimeProvider.reset();

		// extra element from data initialization
		assertThat(messages.findAll()).hasSize(messagesSize + 2);
		assertThat(data.findAll()).hasSize(dataSize + 2);

		deleteJob.deleteMessages();

		assertThat(messages.findAll()).hasSize(messagesSize + 1);
		assertThat(data.findAll()).hasSize(dataSize + 1);
	}

	private void createMessage() {

		var testData = new IrisMessageTestData();

		var folder = this.folders.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX).get();

		messages.save(testData.getTestInboxMessage(folder));
	}
}
