package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.IrisMessageData;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
class IrisMessageDetailsDto {

    private String id;
    private String subject;
    private String body;
    private IrisMessageHdContact hdAuthor;
    private IrisMessageHdContact hdRecipient;
    private Instant createdAt;
    private Boolean isRead;
    private IrisMessageContext context;
    private List<DataAttachment> dataAttachments;
    private IrisMessageDataAttachmentCountDto attachmentCount;

    public static IrisMessageDetailsDto fromEntity(IrisMessage message) {
        List<DataAttachment> dataAttachments = DataAttachment.fromEntity(message.getDataAttachments());
        return new IrisMessageDetailsDto(
                message.getId().toString(),
                message.getSubject(),
                message.getBody(),
                message.getHdAuthor(),
                message.getHdRecipient(),
                message.getMetadata().getCreated(),
                message.isRead(),
                message.getFolder().getContext(),
                dataAttachments,
                IrisMessageDataAttachmentCountDto.fromEntity(message.getDataAttachments())
        );
    }

    @Value
    public static class DataAttachment {

        private String id;
        private String discriminator;
        private String description;
        private Boolean isImported;

        public static List<DataAttachment> fromEntity(List<IrisMessageData> dataList) {
            if (dataList == null) return new ArrayList<>();
            return dataList.stream().map(DataAttachment::fromEntity).collect(Collectors.toList());
        }

        public static DataAttachment fromEntity(IrisMessageData data) {
            return new DataAttachment(
                    data.getId().toString(),
                    data.getDiscriminator(),
                    data.getDescription(),
                    data.isImported()
            );
        }
    }

}
