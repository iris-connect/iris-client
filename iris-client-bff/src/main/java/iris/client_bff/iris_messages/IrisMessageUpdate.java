package iris.client_bff.iris_messages;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IrisMessageUpdate {

    @NotNull
    private Boolean isRead;

}
