package iris.client_bff.iris_messages.eps;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageFile;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.IrisMessageTransfer;
import iris.client_bff.iris_messages.data.IrisMessageData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static iris.client_bff.ui.messages.ErrorMessages.INVALID_INPUT_STRING;

@Service
@RequiredArgsConstructor
public class IrisMessageTransferDefuse {

    private final ValidationHelper validationHelper;

    IrisMessageTransfer defuse(IrisMessageTransfer message) {
        return IrisMessageTransfer.builder()
                .hdAuthor(this.defuse(message.getHdAuthor(), "author"))
                .hdRecipient(this.defuse(message.getHdRecipient(), "recipient"))
                .subject(this.defuse(message.getSubject(), "subject", IrisMessage.SUBJECT_MAX_LENGTH))
                .body(this.defuse(message.getBody(), "body", IrisMessage.BODY_MAX_LENGTH))
                .dataAttachments(this.defuseDataList(message.getDataAttachments()))
                // disabled file attachments
//                .fileAttachments(this.defuseFileList(message.getFileAttachments()))
                .build();
    }

    private IrisMessageTransfer.HdContact defuse(IrisMessageTransfer.HdContact contact, String field) {
        return new IrisMessageTransfer.HdContact()
                .setId(this.defuse(contact.getId(), field + ".id", IrisMessageHdContact.ID_MAX_LENGTH))
                .setName(this.defuse(contact.getName(), field + ".id", IrisMessageHdContact.NAME_MAX_LENGTH));
    }

    private List<IrisMessageTransfer.DataAttachment> defuseDataList(List<IrisMessageTransfer.DataAttachment> dataAttachments) {
        return dataAttachments.stream().map(this::defuse).collect(Collectors.toList());
    }

    private IrisMessageTransfer.DataAttachment defuse(IrisMessageTransfer.DataAttachment dataAttachment) {
        return new IrisMessageTransfer.DataAttachment()
                .setDiscriminator(this.defuse(dataAttachment.getDiscriminator(), "dataAttachment.discriminator", IrisMessageData.DISCRIMINATOR_MAX_LENGTH))
//                .setPayload(this.defuse(dataAttachment.getPayload(), "dataAttachment.payload", IrisMessageData.PAYLOAD_MAX_LENGTH))
                // @todo: create & apply validationHelper for JSON Strings?
                .setPayload(dataAttachment.getPayload())
                .setDescription(this.defuse(dataAttachment.getDescription(), "dataAttachment.description", IrisMessageData.DESCRIPTION_MAX_LENGTH));
    }

    private List<IrisMessageTransfer.FileAttachment> defuseFileList(List<IrisMessageTransfer.FileAttachment> fileAttachments) {
        return fileAttachments.stream().map(this::defuse).collect(Collectors.toList());
    }

    private IrisMessageTransfer.FileAttachment defuse(IrisMessageTransfer.FileAttachment fileAttachment) {
        return new IrisMessageTransfer.FileAttachment()
                .setName(this.defuse(fileAttachment.getName(), "fileAttachment.name", IrisMessageFile.NAME_MAX_LENGTH))
                .setContent(fileAttachment.getContent())
                .setContentType(this.defuse(fileAttachment.getContentType(), "fileAttachment.contentType", IrisMessageFile.CONTENT_TYPE_MAX_LENGTH));
    }

    private String defuse(String input, String field, int maxLength) {
        if (input == null) return null;
        if (this.validationHelper.isPossibleAttack(input, field, true)) {
            return INVALID_INPUT_STRING;
        }
        return StringUtils.truncate(input, maxLength);
    }

}
