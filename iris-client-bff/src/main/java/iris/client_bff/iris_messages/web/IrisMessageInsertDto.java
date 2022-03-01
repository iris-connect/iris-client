package iris.client_bff.iris_messages.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.utils.RawJsonDeserializer;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class IrisMessageInsertDto {

    @NotBlank
    @Size(max = IrisMessageHdContact.ID_MAX_LENGTH)
    private String hdRecipient;

    @NotBlank
    @Size(max = IrisMessage.SUBJECT_MAX_LENGTH)
    private String subject;

    @NotBlank
    @Size(max = IrisMessage.BODY_MAX_LENGTH)
    private String body;

    @Valid
    private List<IrisMessageDataAttachment> dataAttachments;

    @Data
    public static class IrisMessageDataAttachment {

        @NotBlank
        @Size(max = IrisMessageData.DISCRIMINATOR_MAX_LENGTH)
        private String discriminator;

        @NotNull
        @JsonDeserialize(using = RawJsonDeserializer.class)
        private String payload;

        @NotBlank
        @Size(max = IrisMessageData.DESCRIPTION_MAX_LENGTH)
        private String description;

    }
}
