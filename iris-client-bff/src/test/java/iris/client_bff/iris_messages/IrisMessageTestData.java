package iris.client_bff.iris_messages;

import java.util.List;

public class IrisMessageTestData {

    public final IrisMessageFile MOCK_MESSAGE_FILE = getTestMessageFile();

    public final IrisMessageFolder MOCK_DEFAULT_FOLDER = getTestDefaultMessageFolder();

    public final IrisMessageFolder MOCK_INBOX_FOLDER = MOCK_DEFAULT_FOLDER;
    public final IrisMessageFolder MOCK_OUTBOX_FOLDER = getTestMessageFolder(IrisMessageContext.OUTBOX, "outbox folder");

    public final IrisMessageHdContact MOCK_CONTACT_OWN = getTestMessageHdContactOwn();
    public final IrisMessageHdContact MOCK_CONTACT_OTHER = getTestMessageHdContactOther();

    public final IrisMessage MOCK_INBOX_MESSAGE = getTestInboxMessage();
    public final IrisMessage MOCK_OUTBOX_MESSAGE = getTestOutboxMessage();

    public IrisMessage MOCK_MESSAGE_UPDATE = getTestInboxMessage();

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
        folder.setDefaultFolder(folder.getId().toUUID());
        return folder;
    }

    public IrisMessageFolder getTestMessageFolder(IrisMessageFolder parentFolder, String name) {
        return getTestMessageFolder(parentFolder.getContext(), name)
                .setParentFolder(parentFolder.getId().toUUID());
    }

    private IrisMessageFolder getTestMessageFolder(IrisMessageContext context, String name) {
        return new IrisMessageFolder()
                .setContext(context)
                .setName(name)
                .setDefaultFolder(MOCK_DEFAULT_FOLDER.getId().toUUID());
    }

    public IrisMessage getTestOutboxMessage() {
        return this.getTestOutboxMessage(MOCK_OUTBOX_FOLDER);
    }

    public IrisMessage getTestInboxMessage() {
        return this.getTestInboxMessage(MOCK_DEFAULT_FOLDER);
    }

    public IrisMessage getTestOutboxMessage(IrisMessageFolder folder) {
        IrisMessage message = new IrisMessage();
        IrisMessageFile file = getTestMessageFile().setMessage(message);
        message
                .setSubject("Test outbox subject")
                .setBody("Test outbox body")
                .setFolder(folder)
                .setHdAuthor(MOCK_CONTACT_OWN)
                .setHdRecipient(MOCK_CONTACT_OTHER)
                .setAttachments(List.of(file))
                .setIsRead(true);
        return message;
    }

    public IrisMessage getTestInboxMessage(IrisMessageFolder folder) {
        IrisMessage message = new IrisMessage();
        IrisMessageFile file = getTestMessageFile().setMessage(message);
        message
                .setSubject("Test inbox subject")
                .setBody("Test inbox body")
                .setFolder(folder)
                .setHdAuthor(MOCK_CONTACT_OTHER)
                .setHdRecipient(MOCK_CONTACT_OWN)
                .setAttachments(List.of(file))
                .setIsRead(false);
        return message;
    }
}
