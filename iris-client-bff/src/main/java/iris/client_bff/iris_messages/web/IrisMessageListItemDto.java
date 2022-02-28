package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import lombok.Value;

import java.time.Instant;

@Value
class IrisMessageListItemDto {

    private String id;
    private String subject;
    private IrisMessageHdContact hdAuthor;
    private IrisMessageHdContact hdRecipient;
    private Instant createdAt;
    private Boolean isRead;
    private Boolean hasAttachments;

    public static IrisMessageListItemDto fromEntity(IrisMessage message) {
        Boolean hasDataAttachments = message.getDataAttachments() != null && message.getDataAttachments().size() > 0;
        return new IrisMessageListItemDto(
                message.getId().toString(),
                message.getSubject(),
                message.getHdAuthor(),
                message.getHdRecipient(),
                message.getMetadata().getCreated(),
                message.isRead(),
                hasDataAttachments
        );
    }
}
