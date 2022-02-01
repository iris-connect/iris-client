package iris.client_bff.iris_messages;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.data.*;
import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IrisMessageBuilder {

    private final IrisMessageFolderRepository folderRepository;
    private final EPSIrisMessageClient irisMessageClient;

    private final IrisMessageDataProcessors messageDataProcessors;
    private final ValidationHelper validationHelper;

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
            // @todo: enable next line when done with testing
//            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_RECIPIENT);
            // @todo: remove next line when done with testing
            hdRecipient = hdOwn;
        }

        IrisMessage message = new IrisMessage();

        List<IrisMessageData> dataList = new ArrayList<>();
        try {
            if (messageTransfer.getDataAttachments() != null) {
                for ( IrisMessageTransfer.DataAttachment dataAttachment : messageTransfer.getDataAttachments() ) {
                    IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(dataAttachment.getDiscriminator());
                    String payload = processor.payloadFromTransfer(dataAttachment.getPayload());
                    // we do not defuse the payload when receiving it to be able to store yet unknown payload types.
                    // We defuse it while importing / viewing
                    // To minimize the risk of possible attacks, we check the keys & values of the payloads JSON string
                    this.validateMessageDataPayload(payload, dataAttachment.getDiscriminator());
                    IrisMessageData irisMessageData = new IrisMessageData()
                            .setMessage(message)
                            .setDiscriminator(dataAttachment.getDiscriminator())
                            .setDescription(dataAttachment.getDescription())
                            .setPayload(payload);
                    dataList.add(irisMessageData);
                }
            }
        } catch (Throwable e) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_DATA);
        }

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
                .setDataAttachments(dataList)
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

        List<IrisMessageData> dataList = new ArrayList<>();
        try {
            if (messageInsert.getDataAttachments() != null) {
                for ( IrisMessageDataInsert dataInsert : messageInsert.getDataAttachments() ) {
                    IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(dataInsert.getDiscriminator());
                    String payload = processor.payloadFromInsert(dataInsert.getPayload());
                    // The payload should be fine as it is composed of already validated data
                    // Doesn't hurt to validate the keys & values of the payloads JSON string
                    this.validateMessageDataPayload(payload, dataInsert.getDiscriminator());
                    IrisMessageData irisMessageData = new IrisMessageData()
                            .setMessage(message)
                            .setDiscriminator(dataInsert.getDiscriminator())
                            .setDescription(dataInsert.getDescription())
                            .setPayload(payload);
                    dataList.add(irisMessageData);
                }
            }
        } catch (Throwable e) {
            throw new IrisMessageException(ErrorMessages.INVALID_IRIS_MESSAGE_DATA);
        }

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
                .setDataAttachments(dataList)
        // disabled file attachments
//                .setFileAttachments(files)
        ;

        return message;
    }

    private void validateMessageDataPayload(String value, String field) {
        if (validationHelper.isPossibleAttackForMessageDataPayload(value, field, false)) {
            throw new IrisMessageDataException(ErrorMessages.INVALID_IRIS_MESSAGE_DATA + ": "+ field);
        }
    }

}
