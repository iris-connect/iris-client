package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IrisMessageDataProcessors {

	private IrisMessageDataRepository irisMessageDataRepository;

	@Autowired
	public void setIrisMessageDataRepository(IrisMessageDataRepository irisMessageDataRepository) {
		this.irisMessageDataRepository = irisMessageDataRepository;
	}

	private MessageSourceAccessor messages;

	@Autowired
	public void setMessageSourceAccessor(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	private final Map<String, IrisMessageDataProcessor> processors;

	@Autowired
	public IrisMessageDataProcessors(List<IrisMessageDataProcessor> converters) {
		this.processors = converters.stream()
				.collect(Collectors.toMap(IrisMessageDataProcessor::getDiscriminator, Function.identity()));
	}

	public IrisMessageDataProcessor getProcessor(String discriminator) {
		try {
			return this.processors.get(discriminator);
		} catch (Throwable e) {
			throw new IrisMessageDataException(
					messages.getMessage("iris_message.invalid_message_data_discriminator") + ": " + discriminator);
		}
	}

	public IrisMessageDataProcessor getProcessor(IrisMessageData.IrisMessageDataIdentifier messageDataId) {
		try {
			IrisMessageData messageData = this.irisMessageDataRepository.getById(messageDataId);
			return this.processors.get(messageData.getDiscriminator());
		} catch (Throwable e) {
			throw new IrisMessageDataException(
					messages.getMessage("iris_message.invalid_message_data_id") + ": " + messageDataId);
		}
	}

}
