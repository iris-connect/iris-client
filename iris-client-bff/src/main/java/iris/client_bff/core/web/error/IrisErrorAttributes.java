package iris.client_bff.core.web.error;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Jens Kutzsche
 */
@Component
public class IrisErrorAttributes extends DefaultErrorAttributes {

	private static final String ERRORS_KEY = "errors";
	private static final String MESSAGE_KEY = "message";

	private final MessageSourceAccessor messages;
	private final ErrorProperties errorProperties;

	public IrisErrorAttributes(MessageSourceAccessor messages, ServerProperties serverProperties) {

		this.messages = messages;
		this.errorProperties = serverProperties.getError();
	}

	public Map<String, Object> getErrorAttributes(WebRequest request) {
		return getErrorAttributes(request, getErrorAttributeOptions(request));
	}

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

		var errorAttributes = super.getErrorAttributes(webRequest, options);

		Throwable error = getError(webRequest);
		if (error != null) {

			if (error instanceof Errors err) {

				var errors = mapErrors(err);
				setErrorAttribute(errorAttributes, errors);
				setDeterminedMessageAttribute(errorAttributes, errors);

			} else if (error instanceof ConstraintViolationException cve) {

				var violations = mapViolations(cve);
				setErrorAttribute(errorAttributes, violations);
				setDeterminedMessageAttribute(errorAttributes, violations);
			}
		}

		if (!options.isIncluded(Include.BINDING_ERRORS)) {
			errorAttributes.remove(ERRORS_KEY);
		}
		if (!options.isIncluded(Include.MESSAGE)) {
			errorAttributes.remove(MESSAGE_KEY);
		}

		return errorAttributes;
	}

	private Map<String, List<String>> mapErrors(Errors err) {
		return err.getAllErrors().stream().collect(groupingBy(this::mapError,
				mapping(messages::getMessage, toList())));
	}

	private String mapError(ObjectError error) {

		var name = error.getObjectName();

		if (error instanceof FieldError fe) {
			name = fe.getField();
		}

		return name;
	}

	private Map<String, List<String>> mapViolations(ConstraintViolationException cve) {

		return cve.getConstraintViolations().stream()
				.collect(groupingBy(it -> it.getPropertyPath().toString(),
						mapping(ConstraintViolation::getMessage, toList())));
	}

	private void setErrorAttribute(Map<String, Object> errorAttributes, Object content) {
		errorAttributes.put(ERRORS_KEY, content);
	}

	private void setDeterminedMessageAttribute(Map<String, Object> errorAttributes, Map<String, List<String>> errors) {
		errorAttributes.put(MESSAGE_KEY, determineMessage(errors));
	}

	private String determineMessage(Map<String, List<String>> errors) {

		var errorsString = errors.entrySet().stream()
				.map(entry -> String.format("%s ⇒ %s",
						entry.getKey(),
						entry.getValue().stream()
								.collect(Collectors.joining(" + "))))
				.collect(Collectors.joining("; ", "[", "]"));

		return messages.getMessage("iris.exceptions.validation.errors", new Object[] { errorsString });
	}

	private ErrorAttributeOptions getErrorAttributeOptions(WebRequest request) {

		var options = ErrorAttributeOptions.defaults();

		if (this.errorProperties.isIncludeException()) {
			options = options.including(Include.EXCEPTION);
		}
		if (isIncludeAttribute(errorProperties.getIncludeStacktrace(), request, "trace")) {
			options = options.including(Include.STACK_TRACE);
		}
		if (isIncludeAttribute(errorProperties.getIncludeMessage(), request, MESSAGE_KEY)) {
			options = options.including(Include.MESSAGE);
		}
		if (isIncludeAttribute(errorProperties.getIncludeBindingErrors(), request, ERRORS_KEY)) {
			options = options.including(Include.BINDING_ERRORS);
		}
		return options;
	}

	private boolean isIncludeAttribute(IncludeAttribute attribute, WebRequest request, String parameterName) {

		switch (attribute) {
			case ALWAYS:
				return true;
			case ON_PARAM:
				return BooleanUtils.toBoolean(request.getParameter(parameterName));
			default:
				return false;
		}
	}
}
