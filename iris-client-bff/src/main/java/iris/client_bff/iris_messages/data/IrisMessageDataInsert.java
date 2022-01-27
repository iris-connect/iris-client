package iris.client_bff.iris_messages.data;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class IrisMessageDataInsert {

    @NotBlank
    @Size(max = IrisMessageData.DISCRIMINATOR_MAX_LENGTH)
    private String discriminator;

    @NotNull
    private String payload;

    @NotBlank
    @Size(max = IrisMessageData.DESCRIPTION_MAX_LENGTH)
    private String description;

}
