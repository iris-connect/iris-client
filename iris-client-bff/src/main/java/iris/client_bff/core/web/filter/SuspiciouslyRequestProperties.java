package iris.client_bff.core.web.filter;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.valueextraction.Unwrapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.Nullable;
import org.springframework.util.unit.DataSize;

/**
 * @author Jens Kutzsche
 */
@ConfigurationProperties(prefix = "iris.suspiciously.request")
@ConstructorBinding
@RequiredArgsConstructor
public class SuspiciouslyRequestProperties {

	@Autowired
	private Validator validator;

	private final Limits defaults;

	/**
	 * Limits for specific request to declare this as suspicious.
	 */
	@Nullable
	private final Map<@NotBlank String, @Valid Limits> forUri;

	private Map<String, Limits> getForUri() {
		return forUri != null ? forUri : new HashMap<>();
	}

	@PostConstruct
	void validate() {

		var violations = validator.validate(defaults, Default.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		var violations2 = validator.validate(this, ForUri.class);
		if (!violations2.isEmpty()) {
			throw new ConstraintViolationException(violations2);
		}
	}

	public DataSize getContentLengthWarningSizeFor(String uriPath) {

		var warningSize = getForUri().entrySet().stream()
				.filter(it -> uriPath.contains(it.getKey()))
				.map(Entry<String, Limits>::getValue)
				.map(Limits::getContentLengthWarningSize)
				.findFirst();

		return warningSize.orElseGet(defaults::getContentLengthWarningSize);
	}

	public DataSize getContentLengthBlockingSizeFor(String uriPath) {

		var warningSize = getForUri().entrySet().stream()
				.filter(it -> uriPath.contains(it.getKey()))
				.map(Entry<String, Limits>::getValue)
				.map(Limits::getContentLengthBlockingSize)
				.findFirst();

		return warningSize.orElseGet(defaults::getContentLengthBlockingSize);
	}

	@Value
	static class Limits {

		@NotNull(groups = Default.class)
		@Positive(groups = { Default.class, ForUri.class }, payload = Unwrapping.Unwrap.class)
		DataSize contentLengthWarningSize;

		@NotNull(groups = Default.class)
		@Positive(groups = { Default.class, ForUri.class }, payload = Unwrapping.Unwrap.class)
		DataSize contentLengthBlockingSize;
	}

	interface Default {}

	interface ForUri {}
}
