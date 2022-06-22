package iris.client_bff.events.web;

import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.core.messages.ErrorMessages;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
class PluginControllerExceptionHandler {

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = ErrorMessages.EVENT_DATA_REQUEST_CREATION)
	@ExceptionHandler(IRISDataRequestException.class)
	void handleIRISDataRequestException(IRISDataRequestException ex) {
		log.info("Data request failed with exception: {}", getInternalMessage(ex));
	}

	private String getInternalMessage(Exception ex) {

		var msg = defaultString(ex.getMessage());

		if (ex.getCause() != null) {
			msg += ": " + ex.getCause().getMessage();
		}

		return msg;
	}
}
