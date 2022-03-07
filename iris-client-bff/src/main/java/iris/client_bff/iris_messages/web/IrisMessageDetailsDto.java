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
    private DataAttachmentCount attachmentCount;

    public static IrisMessageDetailsDto fromEntity(IrisMessage message) {
        List<DataAttachment> dataAttachments = DataAttachment.fromEntity(message.getDataAttachments());
        DataAttachmentCount count = new DataAttachmentCount(
                dataAttachments.size(),
                dataAttachments.stream().filter((a) -> a.isImported).collect(Collectors.toList()).size()
        );
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
                count
        );
    }

    @Value
    public static class DataAttachmentCount {
        private int total;
        private int imported;
        public static DataAttachmentCount fromEntity(List<IrisMessageData> dataList) {
            List<DataAttachment> dataAttachments = DataAttachment.fromEntity(dataList);
            return new DataAttachmentCount(
                    dataAttachments.size(),
                    (int) dataAttachments.stream().filter((a) -> a.isImported).count()
            );
        }
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
