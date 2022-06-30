package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.web.IrisMessageInsertDto;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class IrisMessageTestData {

	private final IrisMessageDataTestData testData = new IrisMessageDataTestData();

	private final IrisMessageFolder MOCK_DEFAULT_FOLDER = getTestDefaultMessageFolder();

	public final IrisMessageFolder MOCK_INBOX_FOLDER = MOCK_DEFAULT_FOLDER;
	public final IrisMessageFolder MOCK_OUTBOX_FOLDER = getTestMessageFolder(IrisMessageContext.OUTBOX, "outbox folder");

	public final IrisMessageHdContact MOCK_CONTACT_OWN = getTestMessageHdContactOwn();
	public final IrisMessageHdContact MOCK_CONTACT_OTHER = getTestMessageHdContactOther();

	public final IrisMessage MOCK_INBOX_MESSAGE = getTestInboxMessage(MOCK_INBOX_FOLDER);
	public final IrisMessage MOCK_OUTBOX_MESSAGE = getTestOutboxMessage(MOCK_OUTBOX_FOLDER);

	public final static String INVALID_SUBJECT = "S".repeat(Math.max(0, IrisMessage.SUBJECT_MAX_LENGTH + 1));
	public final static String INVALID_BODY = "B".repeat(Math.max(0, IrisMessage.BODY_MAX_LENGTH + 1));

	private IrisMessageHdContact getTestMessageHdContactOwn() {
		return new IrisMessageHdContact()
				.setName("test-own-contact")
				.setId("test-own-contact-id")
				.setOwn(true);
	}

	private IrisMessageHdContact getTestMessageHdContactOther() {
		return new IrisMessageHdContact()
				.setName("test-other-contact")
				.setId("test-other-contact-id")
				.setOwn(false);
	}

	private IrisMessageFolder getTestDefaultMessageFolder() {
		return new IrisMessageFolder()
				.setContext(IrisMessageContext.INBOX)
				.setName("default folder");
	}

	public IrisMessageFolder getTestMessageFolder(IrisMessageFolder parentFolder, String name) {
		return getTestMessageFolder(parentFolder.getContext(), name)
				.setParentFolder(parentFolder.getId());
	}

	private IrisMessageFolder getTestMessageFolder(IrisMessageContext context, String name) {
		return new IrisMessageFolder()
				.setContext(context)
				.setName(name);
	}

	public IrisMessage getTestInboxMessage() {
		IrisMessageFolder folder = this.getTestMessageFolder(IrisMessageContext.INBOX, "inbox folder");
		return this.getTestInboxMessage(folder);
	}

	public IrisMessage getTestOutboxMessage(IrisMessageFolder folder) {
		IrisMessage message = new IrisMessage();
		message
				.setSubject("Test outbox subject")
				.setBody("Test outbox body")
				.setFolder(folder)
				.setHdAuthor(this.getTestMessageHdContactOwn())
				.setHdRecipient(this.getTestMessageHdContactOther())
				.setRead(true)
				.setDataAttachments(List.of(testData.getMessageData(message)));
		return message;
	}

	public IrisMessage getTestInboxMessage(IrisMessageFolder folder) {
		IrisMessage message = new IrisMessage();
		message
				.setSubject("Test inbox subject")
				.setBody("Test inbox body")
				.setFolder(folder)
				.setHdAuthor(this.getTestMessageHdContactOther())
				.setHdRecipient(this.getTestMessageHdContactOwn())
				.setRead(false)
				.setDataAttachments(List.of(testData.getMessageData(message)));
		return message;
	}

	public IrisMessageInsertDto getTestMessageInsert(IrisMessage message) {
		return new IrisMessageInsertDto()
				.setSubject(message.getSubject())
				.setBody(message.getBody())
				.setHdRecipient(message.getHdRecipient().getId())
				.setDataAttachments(List.of(testData.MOCK_EXPORT_DATA_ATTACHMENT));
	}
}
