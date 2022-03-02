package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageData.IrisMessageDataIdentifier;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.IrisMessageDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IrisMessageDataViewProvider {

	private final IrisMessageDataService messageDataService;
	private final IrisMessageDataProcessors messageDataProcessors;

	public IrisMessageDataViewDataDto getViewData(IrisMessageDataIdentifier messageDataId) {
		IrisMessageData messageData = this.messageDataService.getMessageData(messageDataId);
		IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageData.getDiscriminator());
		return this.buildViewData(messageData, processor.getViewPayload(messageData.getPayload()));
	}

	public IrisMessageDataViewDataDto getImportSelectionViewData(
			IrisMessageDataIdentifier messageDataId,
			UUID importTargetId
	) {
		IrisMessageData messageData = this.messageDataService.getMessageData(messageDataId);
		IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageData.getDiscriminator());
		return this.buildViewData(
				messageData,
				processor.getImportSelectionViewPayload(messageData.getPayload(), importTargetId)
		);
	}

	private IrisMessageDataViewDataDto buildViewData(IrisMessageData messageData, Object payload) {
		return new IrisMessageDataViewDataDto()
				.setId(messageData.getId().toString())
				.setDiscriminator(messageData.getDiscriminator())
				.setPayload(payload);
	}

}
