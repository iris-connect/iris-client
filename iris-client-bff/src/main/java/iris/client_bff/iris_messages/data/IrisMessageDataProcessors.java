package iris.client_bff.iris_messages.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IrisMessageDataProcessors {
    private final Map<String, IrisMessageDataProcessor> processors;

    @Autowired
    public IrisMessageDataProcessors(List<IrisMessageDataProcessor> converters) {
        this.processors = converters.stream().collect(Collectors.toMap(IrisMessageDataProcessor::getDiscriminator, Function.identity()));
    }

    public IrisMessageDataProcessor getProcessor(String discriminator) {
        try {
            return this.processors.get(discriminator);
        } catch (Throwable e) {
            throw new IrisMessageDataException("No data processor found for discriminator: " + discriminator);
        }
    }

}
