package iris.client_bff.iris_messages.eps;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static iris.client_bff.ui.messages.ErrorMessages.INVALID_INPUT_STRING;

@Service
@RequiredArgsConstructor
public class IrisMessageTransferDefuse {

    private final ValidationHelper validationHelper;

    IrisMessageTransferDto defuse(IrisMessageTransferDto message) {
        return IrisMessageTransferDto.builder()
                .hdAuthor(this.defuse(message.getHdAuthor(), "author"))
                .hdRecipient(this.defuse(message.getHdRecipient(), "recipient"))
                .subject(this.defuse(message.getSubject(), "subject", IrisMessage.SUBJECT_MAX_LENGTH))
                .body(this.defuse(message.getBody(), "body", IrisMessage.BODY_MAX_LENGTH))
                .build();
    }

    private IrisMessageTransferDto.HdContact defuse(IrisMessageTransferDto.HdContact contact, String field) {
        return new IrisMessageTransferDto.HdContact()
                .setId(this.defuse(contact.getId(), field + ".id", IrisMessageHdContact.ID_MAX_LENGTH))
                .setName(this.defuse(contact.getName(), field + ".id", IrisMessageHdContact.NAME_MAX_LENGTH));
    }

    private String defuse(String input, String field, int maxLength) {
        if (input == null) return null;
        if (this.validationHelper.isPossibleAttack(input, field, true)) {
            return INVALID_INPUT_STRING;
        }
        return StringUtils.truncate(input, maxLength);
    }

}