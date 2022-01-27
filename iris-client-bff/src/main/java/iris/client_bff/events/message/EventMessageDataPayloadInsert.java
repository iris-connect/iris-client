package iris.client_bff.events.message;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Data
public class EventMessageDataPayloadInsert {

    @NotBlank
    UUID id;

    @Valid
    @NotEmpty
    List<@NotBlank UUID> guests;
}
