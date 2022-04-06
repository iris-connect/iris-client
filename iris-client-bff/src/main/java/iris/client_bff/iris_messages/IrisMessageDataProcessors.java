package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
public class IrisMessageDataProcessors {

	private final IrisMessageDataRepository irisMessageDataRepository;

	private final MessageSourceAccessor messages;

	private final Map<String, IrisMessageDataProcessor> processors;

	public IrisMessageDataProcessors(
			IrisMessageDataRepository irisMessageDataRepository,
			MessageSourceAccessor messages,
			List<IrisMessageDataProcessor> converters) {
		this.irisMessageDataRepository = irisMessageDataRepository;
		this.messages = messages;
		this.processors = converters.stream()
				.collect(Collectors.toMap(IrisMessageDataProcessor::getDiscriminator, Function.identity()));
	}

	public IrisMessageDataProcessor withProcessorFor(String discriminator) {
		try {
			return this.processors.get(discriminator);
		} catch (Exception e) {
			throw new IrisMessageDataException(
					messages.getMessage("iris_message.invalid_message_data_discriminator") + ": " + discriminator);
		}
	}

	public IrisMessageDataProcessor withProcessorFor(IrisMessageData.IrisMessageDataIdentifier messageDataId) {
		try {
			IrisMessageData messageData = this.irisMessageDataRepository.getById(messageDataId);
			return this.processors.get(messageData.getDiscriminator());
		} catch (Exception e) {
			throw new IrisMessageDataException(
					messages.getMessage("iris_message.invalid_message_data_id") + ": " + messageDataId);
		}
	}

}
