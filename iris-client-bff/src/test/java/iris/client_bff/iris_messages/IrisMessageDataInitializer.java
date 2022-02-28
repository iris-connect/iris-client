package iris.client_bff.iris_messages;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.DataInitializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(10)
public class IrisMessageDataInitializer implements DataInitializer {

  private final IrisMessageRepository messageRepository;

  private final IrisMessageFolderRepository folderRepository;

  private final IrisMessageTestData testData;

  @Getter
  private IrisMessageFolder inboxFolder;

  @Override
  public void initialize() {

	log.debug("Test data: creating iris messages …");

	Optional<IrisMessageFolder> folder = this.folderRepository
		.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX);
	assertThat(folder).isPresent();
	this.inboxFolder = folder.get();
	IrisMessageFolder nestedInboxFolder = this.testData.getTestMessageFolder(inboxFolder, "nested inbox");

	this.folderRepository.save(nestedInboxFolder);

	Optional<IrisMessageFolder> outboxFolder = this.folderRepository
		.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
	assertThat(outboxFolder).isPresent();

	var message = this.testData.getTestInboxMessage(inboxFolder);
	message.setSubject("First test inbox subject");
	this.messageRepository.save(message);
	this.messageRepository.save(this.testData.getTestInboxMessage(inboxFolder));

	this.messageRepository.save(this.testData.getTestInboxMessage(nestedInboxFolder));

	this.messageRepository.save(this.testData.getTestOutboxMessage(outboxFolder.get()));
  }
}
