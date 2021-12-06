package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Value
public class IrisMessageDetailsDto {

    private String id;
    private String subject;
    private String body;
    private IrisMessageHdContact hdAuthor;
    private IrisMessageHdContact hdRecipient;
    private Instant createdAt;
    private Boolean isRead;
    private List<IrisMessageFileDto> attachments;
    private Boolean hasAttachments;

    public static IrisMessageDetailsDto fromEntity(IrisMessage message) {
        List<IrisMessageFileDto> attachments = message.getAttachments().stream().map(IrisMessageFileDto::fromEntity).toList();
        return new IrisMessageDetailsDto(
                message.getId().toString(),
                message.getSubject(),
                message.getBody(),
                message.getHdAuthor(),
                message.getHdRecipient(),
                message.getMetadata().getCreated(),
                message.getIsRead(),
                attachments,
                attachments.size() > 0
        );
    }
}
