package iris.client_bff.iris_messages.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import iris.client_bff.iris_messages.IrisMessageException;

import javax.validation.Valid;

public interface IrisMessageDataController {
    IrisMessageTransferDto createIrisMessage(
            @Valid @JsonRpcParam(value = "irisMessage") IrisMessageTransferDto messageTransfer
    ) throws IrisMessageException;
}
