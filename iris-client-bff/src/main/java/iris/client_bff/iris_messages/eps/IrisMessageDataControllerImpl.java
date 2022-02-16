package iris.client_bff.iris_messages.eps;

import iris.client_bff.config.JsonRpcDataValidator;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageBuilder;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class IrisMessageDataControllerImpl implements IrisMessageDataController {

	private final IrisMessageTransferDefuse messageTransferDefuse;
	private final IrisMessageService irisMessageService;
	private final IrisMessageBuilder irisMessageBuilder;
	private final MessageSourceAccessor messages;

	private final JsonRpcDataValidator jsonRpcDataValidator;

	@Override
	public IrisMessageTransferDto createIrisMessage(IrisMessageTransferDto messageTransfer) throws ResponseStatusException {
		if (messageTransfer == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, messages.getMessage("iris_message.invalid_id"));
		}
		jsonRpcDataValidator.validateData(messageTransfer);
		try {
			IrisMessage message = this.irisMessageBuilder.build(this.messageTransferDefuse.defuse(messageTransfer));
			IrisMessage savedMessage = this.irisMessageService.saveMessage(message);
			return IrisMessageTransferDto.fromEntity(savedMessage);
		} catch (IrisMessageException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getErrorMessage());
		}
	}
}
