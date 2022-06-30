package iris.client_bff.iris_messages.eps;

import iris.client_bff.iris_messages.exceptions.IrisMessageException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.googlecode.jsonrpc4j.JsonRpcParam;

@Validated
public interface IrisMessageDataController {
	IrisMessageTransferDto createIrisMessage(
			@JsonRpcParam(value = "irisMessage") @NotNull @Valid IrisMessageTransferDto messageTransfer)
			throws IrisMessageException;
}
