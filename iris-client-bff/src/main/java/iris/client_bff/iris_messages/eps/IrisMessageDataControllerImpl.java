package iris.client_bff.iris_messages.eps;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageService;
import iris.client_bff.iris_messages.exceptions.IrisMessageException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IrisMessageDataControllerImpl implements IrisMessageDataController {

	private final IrisMessageService irisMessageService;
	private final IrisMessageBuilderEps irisMessageBuilder;

	@Override
	public IrisMessageTransferDto createIrisMessage(IrisMessageTransferDto messageTransfer) throws IrisMessageException {

		try {
			IrisMessage message = this.irisMessageBuilder.build(messageTransfer);
			IrisMessage savedMessage = this.irisMessageService.saveMessage(message);
			return IrisMessageTransferDto.fromEntity(savedMessage);
		} catch (Exception e) {
			throw new IrisMessageException(e);
		}
	}
}
