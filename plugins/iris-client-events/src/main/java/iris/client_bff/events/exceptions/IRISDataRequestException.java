package iris.client_bff.events.exceptions;

public class IRISDataRequestException extends RuntimeException {

	private static final long serialVersionUID = 3906324468382013579L;

	public IRISDataRequestException() {}

	public IRISDataRequestException(String message) {
		super(message);
	}

	public IRISDataRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public IRISDataRequestException(Throwable cause) {
		super(cause);
	}

	public IRISDataRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
