package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessageFile;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class IrisMessageInsertFileDto {

    @NotBlank
    @Size(max = IrisMessageFile.NAME_MAX_LENGTH)
    private String name;

    @NotBlank
    private String content;
}
