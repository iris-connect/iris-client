package iris.client_bff.hd_search;

import java.io.Serial;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class HdSearchException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -3649251457000674951L;

	public HdSearchException(String failedMethod, Throwable cause) {
		super("Call to '" + failedMethod + "' failed", cause);
	}

	public HdSearchException(String message) {
		super(message);
	}

	public HdSearchException(Throwable cause) {
		super(cause);
	}

	public String getErrorMessage() {
		return ExceptionUtils.getRootCause(this).getMessage();
	}
}
