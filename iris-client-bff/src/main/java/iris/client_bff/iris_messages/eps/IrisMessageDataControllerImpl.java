package iris.client_bff.iris_messages.eps;

import iris.client_bff.config.JsonRpcDataValidator;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IrisMessageDataControllerImpl implements IrisMessageDataController {

	private final IrisMessageTransferDefuse messageTransferDefuse;
	private final IrisMessageService irisMessageService;
	private final IrisMessageBuilderEps irisMessageBuilder;
	private final MessageSourceAccessor messages;

	private final JsonRpcDataValidator jsonRpcDataValidator;

	@Override
	public IrisMessageTransferDto createIrisMessage(IrisMessageTransferDto messageTransfer) throws IrisMessageException {
		if (messageTransfer == null) {
			throw new IrisMessageException(messages.getMessage("iris_message.invalid_id"));
		}
		try {
			jsonRpcDataValidator.validateData(messageTransfer);
			IrisMessage message = this.irisMessageBuilder.build(this.messageTransferDefuse.defuse(messageTransfer));
			IrisMessage savedMessage = this.irisMessageService.saveMessage(message);
			return IrisMessageTransferDto.fromEntity(savedMessage);
		} catch (Throwable e) {
			throw new IrisMessageException(e);
		}
	}
}
