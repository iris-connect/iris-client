package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessageFile;
import lombok.Value;

@Value
public class IrisMessageFileDto {

    private String id;
    private String name;
    private String type;

    public static IrisMessageFileDto fromEntity(IrisMessageFile file) {
        return new IrisMessageFileDto(
                file.getId().toString(),
                file.getName(),
                file.getContentType()
        );
    }
}
