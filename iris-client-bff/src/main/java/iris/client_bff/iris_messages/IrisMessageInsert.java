package iris.client_bff.iris_messages;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class IrisMessageInsert {

    @NotBlank
    private String hdRecipient;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

    private List<MultipartFile> attachments;
}
