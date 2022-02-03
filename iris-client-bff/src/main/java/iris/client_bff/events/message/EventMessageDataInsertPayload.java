package iris.client_bff.events.message;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class EventMessageDataInsertPayload {

    @NotBlank
    String event;

    @Valid
    @NotEmpty
    List<@NotBlank String> guests;
}
