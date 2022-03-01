package iris.client_bff.events.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
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

    public static EventMessageDataInsertPayload toModel(String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(payload, EventMessageDataInsertPayload.class);
        } catch (Throwable e) {
            throw new IrisMessageDataException("invalid payload");
        }
    }
}
