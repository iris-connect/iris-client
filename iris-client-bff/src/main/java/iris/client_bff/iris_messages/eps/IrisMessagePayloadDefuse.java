package iris.client_bff.iris_messages.eps;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageFile;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.IrisMessagePayload;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static iris.client_bff.ui.messages.ErrorMessages.INVALID_INPUT_STRING;

@Service
@RequiredArgsConstructor
public class IrisMessagePayloadDefuse {

    private final ValidationHelper validationHelper;

    IrisMessagePayload defuse(IrisMessagePayload message) {
        return IrisMessagePayload.builder()
                .hdAuthor(this.defuse(message.getHdAuthor(), "author"))
                .hdRecipient(this.defuse(message.getHdRecipient(), "recipient"))
                .subject(this.defuse(message.getSubject(), "subject", IrisMessage.SUBJECT_MAX_LENGTH))
                .body(this.defuse(message.getBody(), "body", IrisMessage.BODY_MAX_LENGTH))
                .attachments(this.defuse(message.getAttachments()))
                .build();
    }

    private IrisMessagePayload.HdContact defuse(IrisMessagePayload.HdContact contact, String field) {
        return new IrisMessagePayload.HdContact()
                .setId(this.defuse(contact.getId(), field + ".id", IrisMessageHdContact.ID_MAX_LENGTH))
                .setName(this.defuse(contact.getName(), field + ".id", IrisMessageHdContact.NAME_MAX_LENGTH));
    }

    private List<IrisMessagePayload.Attachment> defuse(List<IrisMessagePayload.Attachment> attachments) {
        return attachments.stream().map(this::defuse).collect(Collectors.toList());
    }

    private IrisMessagePayload.Attachment defuse(IrisMessagePayload.Attachment attachment) {
        return new IrisMessagePayload.Attachment()
                .setName(this.defuse(attachment.getName(), "attachment.name", IrisMessageFile.NAME_MAX_LENGTH))
                .setContent(attachment.getContent())
                .setContentType(this.defuse(attachment.getContentType(), "attachment.contentType", IrisMessageFile.CONTENT_TYPE_MAX_LENGTH));
    }

    private String defuse(String input, String field, int maxLength) {
        if (this.validationHelper.isPossibleAttack(input, field, true)) {
            return INVALID_INPUT_STRING;
        }
        return StringUtils.truncate(input, maxLength);
    }

}
