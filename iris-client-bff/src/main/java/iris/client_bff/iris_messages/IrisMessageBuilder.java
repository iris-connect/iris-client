package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IrisMessageBuilder {

    private final IrisMessageFolderRepository folderRepository;
    private final EPSIrisMessageClient irisMessageClient;

    public IrisMessage build(IrisMessageTransfer messageTransfer) throws IrisMessageException {

        Optional<IrisMessageFolder> folder = this.folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX);
        if (folder.isEmpty()) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_FOLDER);
        }

        IrisMessageHdContact hdAuthor = new IrisMessageHdContact(
                messageTransfer.getHdAuthor().getId(),
                messageTransfer.getHdAuthor().getName()
        );

        IrisMessageHdContact hdRecipient = new IrisMessageHdContact(
                messageTransfer.getHdRecipient().getId(),
                messageTransfer.getHdRecipient().getName()
        );

        // ensure that the message was sent to the correct recipient
        IrisMessageHdContact hdOwn = this.irisMessageClient.getOwnIrisMessageHdContact();
        if (!Objects.equals(hdOwn.getId(), hdRecipient.getId())) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_RECIPIENT);
        }

        IrisMessage message = new IrisMessage();

        // disabled file attachments
        /*
        List<IrisMessageFile> files = new ArrayList<>();
        try {
            if (messageTransfer.getFileAttachments() != null) {
                Tika tika = new Tika();
                for ( IrisMessageTransfer.FileAttachment file : messageTransfer.getFileAttachments() ) {
                    IrisMessageFile messageFile = new IrisMessageFile()
                            .setMessage(message)
                            .setContent(file.getContent())
                            .setContentType(tika.detect(file.getContent(), file.getName()))
                            .setName(file.getName());
                    files.add(messageFile);
                }
            }
        } catch (Throwable e) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_FILE);
        }
         */

        message
                .setHdAuthor(hdAuthor)
                .setHdRecipient(hdRecipient)
                .setSubject(messageTransfer.getSubject())
                .setBody(messageTransfer.getBody())
                .setFolder(folder.get())
                .setIsRead(false)
        // disabled file attachments
//                .setFileAttachments(files)
        ;

        return message;
    }

    public IrisMessage build(IrisMessageInsert messageInsert) throws IrisMessageException {

        Optional<IrisMessageFolder> folder = this.folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
        if (folder.isEmpty()) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_FOLDER);
        }

        IrisMessageHdContact hdAuthor = this.irisMessageClient.getOwnIrisMessageHdContact();

        Optional<IrisMessageHdContact> hdRecipient = this.irisMessageClient.findIrisMessageHdContactById(messageInsert.getHdRecipient());
        if (hdRecipient.isEmpty()) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_RECIPIENT);
        }

        IrisMessage message = new IrisMessage();

        // disabled file attachments
        /*
        List<IrisMessageFile> files = new ArrayList<>();
        try {
            if (messageInsert.getFileAttachments() != null) {
                Tika tika = new Tika();
                for ( MultipartFile file : messageInsert.getFileAttachments() ) {
                    String fileName = file.getOriginalFilename() == null ? file.getName() : file.getOriginalFilename();
                    IrisMessageFile messageFile = new IrisMessageFile()
                            .setMessage(message)
                            .setContent(file.getBytes())
                            .setContentType(tika.detect(file.getInputStream(), fileName))
                            .setName(fileName);
                    files.add(messageFile);
                }
            }
        } catch (IOException e) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_FILE);
        }
         */

        message
                .setHdAuthor(hdAuthor)
                .setHdRecipient(hdRecipient.get())
                .setSubject(messageInsert.getSubject())
                .setBody(messageInsert.getBody())
                .setFolder(folder.get())
                .setIsRead(true)
        // disabled file attachments
//                .setFileAttachments(files)
        ;

        return message;
    }
}
