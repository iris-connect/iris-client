package iris.client_bff.iris_messages;

import static java.time.Duration.*;
import static org.assertj.core.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.database.IrisDateTimeProvider;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class IrisMessageDeleteJobIntegrationTests {

	private final IrisMessageRepository messageRepo;
	private final IrisMessageDataRepository dataRepo;
	private final IrisMessageFolderRepository folderRepo;
	private final IrisDateTimeProvider dateTimeProvider;
	private final IrisMessageDeleteJob deleteJob;

	@Test
	void testDeleteMessages() {

		var messagesSize = messageRepo.findAll().size();
		var dataSize = dataRepo.findAll().size();

		// in time
		dateTimeProvider.setDelta(ofDays(-179));

		createMessage();

		// to old
		dateTimeProvider.setDelta(ofDays(-181));

		createMessage();

		dateTimeProvider.reset();

		// extra element from data initialization
		assertThat(messageRepo.findAll()).hasSize(messagesSize + 2);
		assertThat(dataRepo.findAll()).hasSize(dataSize + 2);

		deleteJob.deleteMessages();

		assertThat(messageRepo.findAll()).hasSize(messagesSize + 1);
		assertThat(dataRepo.findAll()).hasSize(dataSize + 1);
	}

	private void createMessage() {

		var testData = new IrisMessageTestData();

		var folder = this.folderRepo.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX).get();

		messageRepo.save(testData.getTestInboxMessage(folder));
	}
}
