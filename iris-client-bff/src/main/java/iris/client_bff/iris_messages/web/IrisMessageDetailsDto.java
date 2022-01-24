package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageFile;
import iris.client_bff.iris_messages.IrisMessageHdContact;
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
    private List<Attachment> attachments;
    private Boolean hasAttachments;

    public static IrisMessageDetailsDto fromEntity(IrisMessage message) {
        // disabled file attachments
//        List<Attachment> attachments = Attachment.fromEntity(message.getAttachments());
        List<Attachment> attachments = new ArrayList<>();
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

    @Value
    public static class Attachment {

        private String id;
        private String name;
        private String type;

        public static List<Attachment> fromEntity(List<IrisMessageFile> files) {
            if (files == null) return new ArrayList<>();
            return files.stream().map(Attachment::fromEntity).collect(Collectors.toList());
        }

        public static Attachment fromEntity(IrisMessageFile file) {
            return new Attachment(
                    file.getId().toString(),
                    file.getName(),
                    file.getContentType()
            );
        }
    }

}
