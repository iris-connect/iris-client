package iris.client_bff.iris_messages;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IrisMessageInsert {

    @NotBlank
    private String hdRecipient;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

}
