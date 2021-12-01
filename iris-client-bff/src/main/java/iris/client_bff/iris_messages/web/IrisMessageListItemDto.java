package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageContact;
import lombok.Value;

import java.time.Instant;

@Value
public class IrisMessageListItemDto {

    private String id;
    private String subject;
    private IrisMessageContact authorHd;
    private IrisMessageContact recipientHd;
    private Instant createdAt;
    private Boolean isRead;
    private Boolean hasAttachments;

    public static IrisMessageListItemDto fromEntity(IrisMessage message) {
        return new IrisMessageListItemDto(
                message.getId().toString(),
                message.getSubject(),
                message.getAuthorHd(),
                message.getRecipientHd(),
                message.getMetadata().getCreated(),
                message.getIsRead(),
                message.getHasAttachments()
        );
    }
}
