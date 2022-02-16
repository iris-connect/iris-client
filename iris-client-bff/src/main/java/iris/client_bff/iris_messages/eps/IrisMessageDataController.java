package iris.client_bff.iris_messages.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

public interface IrisMessageDataController {
    IrisMessageTransferDto createIrisMessage(
            @Valid @JsonRpcParam(value = "irisMessage") IrisMessageTransferDto messageTransfer
    ) throws ResponseStatusException;
}
