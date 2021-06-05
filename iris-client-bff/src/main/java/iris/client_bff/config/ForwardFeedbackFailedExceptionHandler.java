package iris.client_bff.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*
 * This class handles exceptions of the post endpoint for post of the class FeedbackRestAPI in the Iris-Client-Backend.
 * @author Ostfalia Gruppe 12
 */
@ControllerAdvice
public class ForwardFeedbackFailedExceptionHandler extends ResponseEntityExceptionHandler {

	/*
	 * Handles the RestClientExcpetion.
	 * @param RestClientException - exception object
	 * @param WebRequest - request object
	 * @return error response with request object
	 */
	@ExceptionHandler(value = {
		RestClientException.class })
	protected ResponseEntity<Object> handleConstraintViolation(RestClientException e, WebRequest request) {
		return handleExceptionInternal(e, "The connection is currently not available.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
