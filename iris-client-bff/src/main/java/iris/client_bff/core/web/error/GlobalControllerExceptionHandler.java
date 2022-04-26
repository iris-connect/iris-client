package iris.client_bff.core.web.error;

import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.core.annotation.AnnotationUtils.*;
import static org.springframework.web.context.request.RequestAttributes.*;

import iris.client_bff.core.messages.ErrorMessages;
import iris.client_bff.core.web.filter.ApplicationRequestSizeLimitFilter.BlockLimitExceededException;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.RequestDispatcher;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private final IrisErrorAttributes errorAttributes;

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value(), SCOPE_REQUEST);

		if (request instanceof ServletWebRequest r) {
			request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, r.getRequest().getRequestURI(), SCOPE_REQUEST);
		}

		if (body == null) {
			body = errorAttributes.getErrorAttributes(request);
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
	ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		return handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(AuthenticationException.class)
	ResponseEntity<?> handleAccessDeniedException(AuthenticationException ex, WebRequest request) {
		var status = HttpStatus.UNAUTHORIZED;
		return handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(AccessDeniedException.class)
	ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) throws Exception {
		var status = HttpStatus.FORBIDDEN;
		return handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<?> handleExceptionFallback(Exception ex, WebRequest request) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let the framework handle it.
		if (isResponseStatusAnnotated(ex) || ex instanceof ResponseStatusException) {
			throw ex;
		}

		log.warn("Unmapped exception occurred", ex);

		var status = HttpStatus.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
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
