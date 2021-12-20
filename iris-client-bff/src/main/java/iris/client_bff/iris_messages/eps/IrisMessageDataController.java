package iris.client_bff.iris_messages.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import iris.client_bff.iris_messages.IrisMessageTransfer;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

public interface IrisMessageDataController {
    IrisMessageTransfer createIrisMessage(
            @Valid @JsonRpcParam(value = "irisMessage") IrisMessageTransfer messageTransfer
    ) throws ResponseStatusException;
}
