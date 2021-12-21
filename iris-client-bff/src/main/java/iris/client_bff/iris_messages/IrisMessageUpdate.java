package iris.client_bff.iris_messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IrisMessageUpdate {

    @NotNull
    private Boolean isRead;

}
