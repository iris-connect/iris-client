package iris.client_bff.iris_messages.eps;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class IrisMessageTransferDto {

    // we send the name & id as we do not know if the author`s health department is accessible by the recipient
    @Valid
    private HdContact hdAuthor;

	// the recipient is used to check if it is identical with the endpoint identifier of the health department, the
	// message is sent to
    @Valid
    private HdContact hdRecipient;

    @NotBlank
    @Size(max = IrisMessage.SUBJECT_MAX_LENGTH)
    private String subject;

    @NotBlank
    @Size(max = IrisMessage.BODY_MAX_LENGTH)
    private String body;

    public static IrisMessageTransferDto fromEntity(IrisMessage message) {
        return IrisMessageTransferDto.builder()
                .hdAuthor(HdContact.fromEntity(message.getHdAuthor()))
                .hdRecipient(HdContact.fromEntity(message.getHdRecipient()))
                .subject(message.getSubject())
                .body(message.getBody())
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
}
