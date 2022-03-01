package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.IrisMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IrisMessageDataViewProvider {

	private final IrisMessageService messageService;
	private final IrisMessageDataProcessors messageDataProcessors;

	public IrisMessageDataViewDataDto getViewData(IrisMessageData.IrisMessageDataIdentifier messageDataId) {
		IrisMessageData messageData = this.messageService.getMessageData(messageDataId);
		IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageData.getDiscriminator());
		return new IrisMessageDataViewDataDto().setId(messageData.getId().toString())
				.setDiscriminator(messageData.getDiscriminator())
				.setPayload(processor.getViewPayload(messageData.getPayload()));
	}

	public IrisMessageDataViewDataDto getViewData(
			IrisMessageData.IrisMessageDataIdentifier messageDataId, UUID importTargetId) {
		IrisMessageData messageData = this.messageService.getMessageData(messageDataId);
		IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageData.getDiscriminator());
		return new IrisMessageDataViewDataDto().setId(messageData.getId().toString())
				.setDiscriminator(messageData.getDiscriminator())
				.setPayload(processor.getImportSelectionViewPayload(messageData.getPayload(), importTargetId));
	}

}
