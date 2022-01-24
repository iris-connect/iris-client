package iris.client_bff.iris_messages;

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

    // disabled file attachments
//    @Valid
//    private List<Attachment> attachments;

    public static IrisMessageTransfer fromEntity(IrisMessage message) {
        return IrisMessageTransfer.builder()
                .hdAuthor(HdContact.fromEntity(message.getHdAuthor()))
                .hdRecipient(HdContact.fromEntity(message.getHdRecipient()))
                .subject(message.getSubject())
                .body(message.getBody())
                // disabled file attachments
//                .attachments(Attachment.fromEntity(message.getAttachments()))
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
    public static class Attachment {

        @NotBlank
        @Size(max = IrisMessageFile.NAME_MAX_LENGTH)
        private String name;

        private byte[] content;

        /**
         * @deprecated
         */
        @Size(max = IrisMessageFile.CONTENT_TYPE_MAX_LENGTH)
        private String contentType;

        public static List<Attachment> fromEntity(List<IrisMessageFile> files) {
            return files.stream().map(Attachment::fromEntity).collect(Collectors.toList());
        }

        public static Attachment fromEntity(IrisMessageFile file) {
            return new Attachment()
                    .setName(file.getName())
                    .setContent(file.getContent())
                    .setContentType(file.getContentType());
        }
    }

}
