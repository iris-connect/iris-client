package iris.client_bff.iris_messages.eps;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageFile;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IrisMessageTransferDto {

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
    private List<FileAttachment> fileAttachments;

    public static IrisMessageTransferDto fromEntity(IrisMessage message) {
        return IrisMessageTransferDto.builder()
                .hdAuthor(HdContact.fromEntity(message.getHdAuthor()))
                .hdRecipient(HdContact.fromEntity(message.getHdRecipient()))
                .subject(message.getSubject())
                .body(message.getBody())
                .fileAttachments(FileAttachment.fromEntity(message.getFileAttachments()))
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
    public static class FileAttachment {

        @NotBlank
        @Size(max = IrisMessageFile.NAME_MAX_LENGTH)
        private String name;

        private String content;

        public static List<FileAttachment> fromEntity(List<IrisMessageFile> files) {
            return files.stream().map(FileAttachment::fromEntity).collect(Collectors.toList());
        }

        public static FileAttachment fromEntity(IrisMessageFile file) {
            return new FileAttachment()
                    .setName(file.getName())
                    .setContent(Base64.getEncoder().encodeToString(file.getContent()));
        }
    }

}
