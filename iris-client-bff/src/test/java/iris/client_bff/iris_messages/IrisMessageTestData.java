package iris.client_bff.iris_messages;

import org.springframework.stereotype.Component;

@Component
public class IrisMessageTestData {

	public final IrisMessageFile MOCK_MESSAGE_FILE = getTestMessageFile();

	private final IrisMessageFolder MOCK_DEFAULT_FOLDER = getTestDefaultMessageFolder();

	public final IrisMessageFolder MOCK_INBOX_FOLDER = MOCK_DEFAULT_FOLDER;
	public final IrisMessageFolder MOCK_OUTBOX_FOLDER = getTestMessageFolder(IrisMessageContext.OUTBOX, "outbox folder");

	public final IrisMessageHdContact MOCK_CONTACT_OWN = getTestMessageHdContactOwn();
	public final IrisMessageHdContact MOCK_CONTACT_OTHER = getTestMessageHdContactOther();

	public final IrisMessage MOCK_INBOX_MESSAGE = getTestInboxMessage(MOCK_INBOX_FOLDER);
	public final IrisMessage MOCK_OUTBOX_MESSAGE = getTestOutboxMessage(MOCK_OUTBOX_FOLDER);

	public final static String INVALID_SUBJECT = "S".repeat(Math.max(0, IrisMessage.SUBJECT_MAX_LENGTH + 1));
	public final static String INVALID_BODY = "B".repeat(Math.max(0, IrisMessage.BODY_MAX_LENGTH + 1));

	private IrisMessageFile getTestMessageFile() {
		return new IrisMessageFile()
				.setName("test-file-name")
				.setContent("test".getBytes())
				.setContentType("text/plain");
	}

	private IrisMessageHdContact getTestMessageHdContactOwn() {
		return new IrisMessageHdContact()
				.setName("test-own-contact")
				.setId("test-own-contact-id")
				.setIsOwn(true);
	}

	private IrisMessageHdContact getTestMessageHdContactOther() {
		return new IrisMessageHdContact()
				.setName("test-other-contact")
				.setId("test-other-contact-id")
				.setIsOwn(false);
	}

	private IrisMessageFolder getTestDefaultMessageFolder() {
		IrisMessageFolder folder = new IrisMessageFolder()
				.setContext(IrisMessageContext.INBOX)
				.setName("default folder");
		folder.setDefaultFolder(folder.getId());
		return folder;
	}

	public IrisMessageFolder getTestMessageFolder(IrisMessageFolder parentFolder, String name) {
		return getTestMessageFolder(parentFolder.getContext(), name)
				.setParentFolder(parentFolder.getId());
	}

	private IrisMessageFolder getTestMessageFolder(IrisMessageContext context, String name) {
		return new IrisMessageFolder()
				.setContext(context)
				.setName(name)
				.setDefaultFolder(MOCK_DEFAULT_FOLDER.getId());
	}

	public IrisMessage getTestInboxMessage() {
		IrisMessageFolder folder = this.getTestMessageFolder(IrisMessageContext.INBOX, "inbox folder");
		return this.getTestInboxMessage(folder);
	}

	public IrisMessage getTestOutboxMessage(IrisMessageFolder folder) {
		IrisMessage message = new IrisMessage();
		// disabled file attachments
		// IrisMessageFile file = getTestMessageFile().setMessage(message);
		message
				.setSubject("Test outbox subject")
				.setBody("Test outbox body")
				.setFolder(folder)
				.setHdAuthor(this.getTestMessageHdContactOwn())
				.setHdRecipient(this.getTestMessageHdContactOther())
				// disabled file attachments
				// .setFileAttachments(List.of(file))
				.setIsRead(true);
		return message;
	}

	public IrisMessage getTestInboxMessage(IrisMessageFolder folder) {
		IrisMessage message = new IrisMessage();
		// disabled file attachments
		// IrisMessageFile file = getTestMessageFile().setMessage(message);
		message
				.setSubject("Test inbox subject")
				.setBody("Test inbox body")
				.setFolder(folder)
				.setHdAuthor(this.getTestMessageHdContactOther())
				.setHdRecipient(this.getTestMessageHdContactOwn())
				// disabled file attachments
				// .setFileAttachments(List.of(file))
				.setIsRead(false);
		return message;
	}

	public IrisMessageInsert getTestMessageInsert(IrisMessage message) {
		// disabled file attachments
		/*
		List<MultipartFile> files = new ArrayList<>();
		for ( IrisMessageFile messageFile : message.getFileAttachments() ) {
		    files.add(
		            new MockMultipartFile(
		                    messageFile.getName() + "_form",
		                    messageFile.getName(),
		                    messageFile.getContentType(),
		                    messageFile.getContent()
		            )
		    );
		}
		 */
		return new IrisMessageInsert()
				.setSubject(message.getSubject())
				.setBody(message.getBody())
				.setHdRecipient(message.getHdRecipient().getId())
		// disabled file attachments
		// .setFileAttachments(files)
		;
	}
}
