package iris.client_bff.core.web.error;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
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
	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

		var errorAttributes = super.getErrorAttributes(webRequest, options);

		Throwable error = getError(webRequest);
		if (error != null) {

			if (error instanceof Errors br) {
				setErrorAttribute(errorAttributes, mapErrors(br));
			} else if (error instanceof ConstraintViolationException cve) {
				setErrorAttribute(errorAttributes, mapViolations(cve));
			}
		}

		if (!options.isIncluded(Include.BINDING_ERRORS)) {
			errorAttributes.remove("errors");
		}

		return errorAttributes;
	}

	private Map<String, List<ObjectError>> mapErrors(Errors br) {
		return br.getAllErrors().stream().collect(groupingBy(this::mapError));
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

	private Object setErrorAttribute(Map<String, Object> errorAttributes, Object content) {
		return errorAttributes.put("errors", content);
	}
}
