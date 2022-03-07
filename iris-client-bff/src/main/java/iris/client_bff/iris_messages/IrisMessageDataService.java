package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IrisMessageDataService {

    private final IrisMessageDataRepository messageDataRepository;
    private final IrisMessageDataProcessors messageDataProcessors;

    public IrisMessageData getMessageData(IrisMessageData.IrisMessageDataIdentifier messageDataId) {
        Optional<IrisMessageData> optionalIrisMessageData = this.messageDataRepository.findById(messageDataId);
        if (optionalIrisMessageData.isEmpty()) {
            throw new IrisMessageDataException("iris_message.missing_message_data");
        }
        return optionalIrisMessageData.get();
    }

    public void importMessageData(IrisMessageData.IrisMessageDataIdentifier messageDataId) {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageDataId);
        processor.importPayload(messageData.getPayload());
        messageData.setImported(true);
        this.messageDataRepository.save(messageData);
    }

    public void importMessageData(IrisMessageData.IrisMessageDataIdentifier messageDataId, UUID importTargetId, String importSelection) {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageDataId);
        processor.importPayload(
                messageData.getPayload(),
                importTargetId,
                importSelection
        );
        messageData.setImported(true);
        this.messageDataRepository.save(messageData);
    }

}
