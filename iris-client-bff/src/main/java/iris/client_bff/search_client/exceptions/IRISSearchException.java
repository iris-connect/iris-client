package iris.client_bff.search_client.exceptions;

public class IRISSearchException extends RuntimeException {

	private static final long serialVersionUID = -4937902884117098576L;

	public IRISSearchException(String failedMethod, Throwable cause) {
		super("Call to '" + failedMethod + "' failed", cause);
	}

	public IRISSearchException(Throwable cause) {
		super(cause);
	}
}
