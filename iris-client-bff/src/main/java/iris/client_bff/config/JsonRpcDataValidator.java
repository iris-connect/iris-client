package iris.client_bff.config;

import iris.client_bff.core.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Configuration
@RequiredArgsConstructor
public class JsonRpcDataValidator {

	private final Validator validator;

	public void validateData(Object... data) {

		Arrays.stream(data)
				.map(validator::validate)
				.flatMap(Set::stream)
				.map(this::createViolationPair)
				.reduce(this::joinViolations)
				.ifPresent(this::throwException);
	}

	public void validateData(Object data) {

		validator.validate(data).stream()
				.map(this::createViolationPair)
				.reduce(this::joinViolations)
				.ifPresent(this::throwException);
	}

	private String createViolationPair(ConstraintViolation<Object> violation) {
		return String.format("%s: %s", violation.getPropertyPath(), violation.getMessage());
	}

	private String joinViolations(String a, String b) {
		return String.join(", ", a, b);
	}

	private void throwException(String it) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT + ": " + it);
	}
}
