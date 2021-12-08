package iris.client_bff.iris_messages.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import iris.client_bff.iris_messages.IrisMessagePayload;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

public interface IrisMessageDataController {
    void createIrisMessage(
            @Valid @JsonRpcParam(value = "irisMessage") IrisMessagePayload irisMessage
    ) throws ResponseStatusException;
}
