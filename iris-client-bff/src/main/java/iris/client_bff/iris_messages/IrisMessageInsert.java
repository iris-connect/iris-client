package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.validation.FileTypeConstraint;
import iris.client_bff.iris_messages.validation.IrisMessageFileConstraint;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class IrisMessageInsert {

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
    private List<@IrisMessageFileConstraint @FileTypeConstraint MultipartFile> attachments;
}
