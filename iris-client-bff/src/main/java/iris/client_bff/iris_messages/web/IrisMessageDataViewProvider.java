package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageData.IrisMessageDataIdentifier;
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
		var payload = this.messageDataProcessors.withProcessorFor(messageData.getDiscriminator())
				.getViewPayload(messageData.getPayload());
		return this.buildViewData(messageData, payload);
	}

	public IrisMessageDataViewDataDto getImportSelectionViewData(
			IrisMessageDataIdentifier messageDataId,
			UUID importTargetId
	) {
		IrisMessageData messageData = this.messageDataService.getMessageData(messageDataId);
		var payload = this.messageDataProcessors.withProcessorFor(messageData.getDiscriminator())
				.getImportSelectionViewPayload(messageData.getPayload(), importTargetId);
		return this.buildViewData(messageData, payload);
	}

	private IrisMessageDataViewDataDto buildViewData(IrisMessageData messageData, Object payload) {
		return new IrisMessageDataViewDataDto()
				.setId(messageData.getId().toString())
				.setDiscriminator(messageData.getDiscriminator())
				.setPayload(payload);
	}

}
