package iris.client_bff.iris_messages;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class IrisMessageFolderInitializer {

	private IrisMessageFolderRepository folderRepository;

	@PostConstruct
	protected void createMessageFoldersIfNotExist() {
		if (folderRepository.findAll().isEmpty()) {
			IrisMessageFolder inboxFolder = new IrisMessageFolder();
			inboxFolder
					.setName("Posteingang")
					.setDefaultFolder(inboxFolder.getId())
					.setContext(IrisMessageContext.INBOX);
			IrisMessageFolder outboxFolder = new IrisMessageFolder();
			outboxFolder
					.setName("Postausgang")
					.setDefaultFolder(inboxFolder.getId())
					.setContext(IrisMessageContext.OUTBOX);
			folderRepository.save(inboxFolder);
			folderRepository.save(outboxFolder);
		} else {
			log.info("Initial iris message folders already exists. Skip creating of folders.");
		}
	}
}
