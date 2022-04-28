package iris.client_bff.iris_messages.utils;

import iris.client_bff.core.messages.ErrorMessages;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class IrisMessageDataUtils {

	private final Validator validator;
	private final ObjectMapper objectMapper;

	public <T> void validatePayload(T payload) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(payload);
		if (!constraintViolations.isEmpty()) {
			throw new IrisMessageDataException(ErrorMessages.INVALID_INPUT + ": "
					+ constraintViolations.stream().map(
							violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
							.collect(Collectors.joining(", ")));
		}
	}

	public <T> T parseJSON(String value, Class<T> valueType) {
		try {
			return objectMapper.readValue(value, valueType);
		} catch (Exception e) {
			throw new IrisMessageDataException("iris_message.invalid_message_data");
		}
	}

	public <T> String stringifyJSON(T value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new IrisMessageDataException("iris_message.invalid_message_data");
		}
	}

}
