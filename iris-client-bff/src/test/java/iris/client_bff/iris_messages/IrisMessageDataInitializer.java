package iris.client_bff.iris_messages;

import iris.client_bff.DataInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(10)
public class IrisMessageDataInitializer implements DataInitializer {

	private final IrisMessageRepository messageRepository;

	private final IrisMessageFolderRepository folderRepository;

	private final IrisMessageTestData testData;

	@Override
	public void initialize() {

		log.debug("Test data: creating iris messages …");

		Optional<IrisMessageFolder> inboxFolder = folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX);
		assertThat(inboxFolder.isPresent()).isTrue();
		IrisMessageFolder nestedInboxFolder = testData.getTestMessageFolder(inboxFolder.get(), "nested inbox");

		folderRepository.save(nestedInboxFolder);

		Optional<IrisMessageFolder> outboxFolder = folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
		assertThat(outboxFolder.isPresent()).isTrue();

		messageRepository.save(testData.getTestInboxMessage(inboxFolder.get()));
		messageRepository.save(testData.getTestInboxMessage(inboxFolder.get()));

		messageRepository.save(testData.getTestInboxMessage(nestedInboxFolder));

		messageRepository.save(testData.getTestOutboxMessage(outboxFolder.get()));

	}
}
