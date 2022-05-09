package iris.client_bff.iris_messages.exceptions;

import java.io.Serial;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class IrisMessageException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 8068203942662884747L;

	public IrisMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public IrisMessageException(String message) {
		super(message);
	}

	public IrisMessageException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public String getErrorMessage() {
		return ExceptionUtils.getRootCause(this).getMessage();
	}
}
