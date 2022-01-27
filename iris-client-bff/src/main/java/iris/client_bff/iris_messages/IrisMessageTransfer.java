package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.data.IrisMessageData;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IrisMessageTransfer {

    // we send the name & id as we do not know if the author`s health department is accessible by the recipient
    @Valid
    private HdContact hdAuthor;

    // the recipient is used to check if it is identical with the endpoint identifier of the health department, the message is sent to
    @Valid
    private HdContact hdRecipient;

    @NotBlank
    @Size(max = IrisMessage.SUBJECT_MAX_LENGTH)
    private String subject;

    @NotBlank
    @Size(max = IrisMessage.BODY_MAX_LENGTH)
    private String body;

    @Valid
    private List<DataAttachment> dataAttachments;

    // disabled file attachments
//    @Valid
//    private List<FileAttachment> fileAttachments;

    public static IrisMessageTransfer fromEntity(IrisMessage message) {
        return IrisMessageTransfer.builder()
                .hdAuthor(HdContact.fromEntity(message.getHdAuthor()))
                .hdRecipient(HdContact.fromEntity(message.getHdRecipient()))
                .subject(message.getSubject())
                .body(message.getBody())
                .dataAttachments(DataAttachment.fromEntity(message.getDataAttachments()))
                // disabled file attachments
//                .fileAttachments(FileAttachment.fromEntity(message.getFileAttachments()))
                .build();
    }

    @Data
    public static class HdContact {

        @NotBlank
        @Size(max = IrisMessageHdContact.ID_MAX_LENGTH)
        private String id;

        @NotBlank
        @Size(max = IrisMessageHdContact.NAME_MAX_LENGTH)
        private String name;

        public static HdContact fromEntity(IrisMessageHdContact contact) {
            return new HdContact()
                    .setId(contact.getId())
                    .setName(contact.getName());
        }
    }

    @Data
    public static class DataAttachment {

        @NotBlank
        @Size(max = IrisMessageData.DISCRIMINATOR_MAX_LENGTH)
        private String discriminator;

        @NotBlank
        @Size(max = IrisMessageData.PAYLOAD_MAX_LENGTH)
        private String payload;

        @NotBlank
        @Size(max = IrisMessageData.DESCRIPTION_MAX_LENGTH)
        private String description;

        public static List<DataAttachment> fromEntity(List<IrisMessageData> dataList) {
            return dataList.stream().map(DataAttachment::fromEntity).collect(Collectors.toList());
        }

        public static DataAttachment fromEntity(IrisMessageData data) {
            return new DataAttachment()
                    .setDiscriminator(data.getDiscriminator())
                    .setPayload(data.getPayload())
                    .setDescription(data.getDescription());
        }
    }

    @Data
    public static class FileAttachment {

        @NotBlank
        @Size(max = IrisMessageFile.NAME_MAX_LENGTH)
        private String name;

        private byte[] content;

        /**
         * @deprecated
         */
        @Size(max = IrisMessageFile.CONTENT_TYPE_MAX_LENGTH)
        private String contentType;

        public static List<FileAttachment> fromEntity(List<IrisMessageFile> files) {
            return files.stream().map(FileAttachment::fromEntity).collect(Collectors.toList());
        }

        public static FileAttachment fromEntity(IrisMessageFile file) {
            return new FileAttachment()
                    .setName(file.getName())
                    .setContent(file.getContent())
                    .setContentType(file.getContentType());
        }
    }

}
