package iris.client_bff.core.web.error;

import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.core.annotation.AnnotationUtils.*;
import static org.springframework.web.context.request.RequestAttributes.*;

import iris.client_bff.core.messages.ErrorMessages;
import iris.client_bff.core.web.filter.ApplicationRequestSizeLimitFilter.BlockLimitExceededException;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.RequestDispatcher;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private final ErrorProperties errorProperties;
	private final ErrorAttributes errorAttributes;

	GlobalControllerExceptionHandler(ServerProperties serverProperties, ErrorAttributes errorAttributes) {

		this.errorProperties = serverProperties.getError();
		this.errorAttributes = errorAttributes;
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value(), SCOPE_REQUEST);

		if (body == null) {
			body = errorAttributes.getErrorAttributes(request, getErrorAttributeOptions(request));
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = ErrorMessages.LOCATION_SEARCH)
	@ExceptionHandler(IRISSearchException.class)
	void handleIRISSearchException(IRISSearchException ex) {
		log.info("Search failed with exception: {}", getInternalMessage(ex));
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = ErrorMessages.EVENT_DATA_REQUEST_CREATION)
	@ExceptionHandler(IRISDataRequestException.class)
	void handleIRISDataRequestException(IRISDataRequestException ex) {
		log.info("Data request failed with exception: {}", getInternalMessage(ex));
	}

	@ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE, reason = ErrorMessages.REQUEST_TOO_LARGE)
	@ExceptionHandler(BlockLimitExceededException.class)
	void handleBlockLimitExceededException(BlockLimitExceededException ex) {
		log.info("Request failed with exception: {}", getInternalMessage(ex));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		return handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
	}

	@ExceptionHandler({ ResponseStatusException.class, AccessDeniedException.class, AuthenticationException.class })
	void rethrowHandledFrameworkException(Exception ex) throws Exception {
		// let the framework handle this exceptions
		throw ex;
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<?> handleExceptionFallback(Exception ex, WebRequest request) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let the framework handle it.
		if (isResponseStatusAnnotated(ex)) {
			throw ex;
		}

		log.warn("Unmapped exception occurred", ex);

		var status = HttpStatus.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
	}

	private ErrorAttributeOptions getErrorAttributeOptions(WebRequest request) {

		var options = ErrorAttributeOptions.defaults();

		if (this.errorProperties.isIncludeException()) {
			options = options.including(Include.EXCEPTION);
		}
		if (isIncludeAttribute(errorProperties.getIncludeStacktrace(), request, "trace")) {
			options = options.including(Include.STACK_TRACE);
		}
		if (isIncludeAttribute(errorProperties.getIncludeMessage(), request, "message")) {
			options = options.including(Include.MESSAGE);
		}
		if (isIncludeAttribute(errorProperties.getIncludeBindingErrors(), request, "errors")) {
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

	private String getInternalMessage(Exception ex) {

		var msg = defaultString(ex.getMessage());

		if (ex.getCause() != null) {
			msg += ": " + ex.getCause().getMessage();
		}

		return msg;
	}

	private boolean isResponseStatusAnnotated(Exception ex) {
		return findAnnotation(ex.getClass(), ResponseStatus.class) != null;
	}
}
