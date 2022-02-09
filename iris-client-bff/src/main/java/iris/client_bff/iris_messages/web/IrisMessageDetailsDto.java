package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.IrisMessageFile;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.data.IrisMessageData;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class IrisMessageDetailsDto {

    private String id;
    private String subject;
    private String body;
    private IrisMessageHdContact hdAuthor;
    private IrisMessageHdContact hdRecipient;
    private Instant createdAt;
    private Boolean isRead;
    private IrisMessageContext context;
    private List<DataAttachment> dataAttachments;
    private List<FileAttachment> fileAttachments;
    private Boolean hasAttachments;

    public static IrisMessageDetailsDto fromEntity(IrisMessage message) {
        // disabled file attachments
//        List<FileAttachment> fileAttachments = FileAttachment.fromEntity(message.getFileAttachments());
        List<FileAttachment> fileAttachments = new ArrayList<>();
        List<DataAttachment> dataAttachments = DataAttachment.fromEntity(message.getDataAttachments());
        return new IrisMessageDetailsDto(
                message.getId().toString(),
                message.getSubject(),
                message.getBody(),
                message.getHdAuthor(),
                message.getHdRecipient(),
                message.getMetadata().getCreated(),
                message.getIsRead(),
                message.getFolder().getContext(),
                dataAttachments,
                fileAttachments,
                dataAttachments.size() > 0 || fileAttachments.size() > 0
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
                    data.getIsImported()
            );
        }
    }

    @Value
    public static class FileAttachment {

        private String id;
        private String name;
        private String type;

        public static List<FileAttachment> fromEntity(List<IrisMessageFile> files) {
            if (files == null) return new ArrayList<>();
            return files.stream().map(FileAttachment::fromEntity).collect(Collectors.toList());
        }

        public static FileAttachment fromEntity(IrisMessageFile file) {
            return new FileAttachment(
                    file.getId().toString(),
                    file.getName(),
                    file.getContentType()
            );
        }
    }

}
