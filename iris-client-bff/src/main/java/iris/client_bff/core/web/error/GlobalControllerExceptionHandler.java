package iris.client_bff.core.web.error;

import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = ErrorMessages.LOCATION_SEARCH)
	@ExceptionHandler(IRISSearchException.class)
	public void handleIRISSearchException(IRISSearchException ex) {
		log.info("Search failed with exception: {}", getInternalMessage(ex));
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = ErrorMessages.EVENT_DATA_REQUEST_CREATION)
	@ExceptionHandler(IRISDataRequestException.class)
	public void handleIRISDataRequestException(IRISDataRequestException ex) {
		log.info("Data request failed with exception: {}", getInternalMessage(ex));
	}

	@ExceptionHandler(Exception.class)
	public void handleException(Exception ex) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let the framework handle it.
		if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
			throw ex;

		log.warn("Unmapped exception occurred", ex);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getInternalMessage(ex));
	}

	private String getInternalMessage(Exception ex) {

		var msg = defaultString(ex.getMessage());

		if (ex.getCause() != null) {
			msg += ": " + ex.getCause().getMessage();
		}

		return msg;
	}
}
