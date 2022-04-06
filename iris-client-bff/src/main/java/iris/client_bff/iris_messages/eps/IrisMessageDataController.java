package iris.client_bff.iris_messages.eps;

import iris.client_bff.iris_messages.exceptions.IrisMessageException;

import javax.validation.Valid;

import com.googlecode.jsonrpc4j.JsonRpcParam;

public interface IrisMessageDataController {
	IrisMessageTransferDto createIrisMessage(
			@Valid @JsonRpcParam(value = "irisMessage") IrisMessageTransferDto messageTransfer) throws IrisMessageException;
}
