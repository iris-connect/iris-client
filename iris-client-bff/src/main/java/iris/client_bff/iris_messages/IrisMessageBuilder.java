package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IrisMessageBuilder {

    private final IrisMessageFolderRepository folderRepository;
    private final EPSIrisMessageClient irisMessageClient;

    public IrisMessage build(IrisMessageTransfer messageTransfer) throws IrisMessageException {

        IrisMessageFolder folder = this.folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX);
        if (folder == null) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_FOLDER);
        }

        IrisMessageHdContact hdAuthor = new IrisMessageHdContact(
                messageTransfer.getHdAuthor().getId(),
                messageTransfer.getHdAuthor().getName()
        );

        IrisMessageHdContact hdRecipient = this.irisMessageClient.getOwnIrisMessageHdContact();
        // ensure that the message was sent to the correct recipient
        // @todo: enable this when done with testing
//        if (!Objects.equals(hdRecipient.getId(), messageTransfer.getHdRecipient().getId())) {
//            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_RECIPIENT);
//        }

        IrisMessage message = new IrisMessage();

        List<IrisMessageFile> files = new ArrayList<>();
        if (messageTransfer.getAttachments() != null) {
            for ( IrisMessageTransfer.Attachment file : messageTransfer.getAttachments() ) {
                IrisMessageFile messageFile = new IrisMessageFile()
                        .setMessage(message)
                        .setContent(file.getContent())
                        .setContentType(file.getContentType())
                        .setName(file.getName());
                files.add(messageFile);
            }
        }

        message
                .setHdAuthor(hdAuthor)
                .setHdRecipient(hdRecipient)
                .setSubject(messageTransfer.getSubject())
                .setBody(messageTransfer.getBody())
                .setFolder(folder)
                .setIsRead(false)
                .setAttachments(files);

        return message;
    }

    public IrisMessage build(IrisMessageInsert messageInsert) throws IrisMessageException {

        IrisMessageFolder folder = this.folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
        if (folder == null) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_FOLDER);
        }

        IrisMessageHdContact hdAuthor = this.irisMessageClient.getOwnIrisMessageHdContact();

        IrisMessageHdContact hdRecipient = this.irisMessageClient.findIrisMessageHdContactById(messageInsert.getHdRecipient());
        if (hdRecipient == null) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_RECIPIENT);
        }

        IrisMessage message = new IrisMessage();

        List<IrisMessageFile> files = new ArrayList<>();
        try {
            if (messageInsert.getAttachments() != null) {
                for ( MultipartFile file : messageInsert.getAttachments() ) {
                    IrisMessageFile messageFile = new IrisMessageFile()
                            .setMessage(message)
                            .setContent(file.getBytes())
                            .setContentType(file.getContentType())
                            .setName(file.getOriginalFilename());
                    files.add(messageFile);
                }
            }
        } catch (IOException e) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_FILE);
        }

        message
                .setHdAuthor(hdAuthor)
                .setHdRecipient(hdRecipient)
                .setSubject(messageInsert.getSubject())
                .setBody(messageInsert.getBody())
                .setFolder(folder)
                .setIsRead(true)
                .setAttachments(files);

        return message;
    }
}
