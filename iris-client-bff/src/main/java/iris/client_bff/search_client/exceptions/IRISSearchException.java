package iris.client_bff.search_client.exceptions;

public class IRISSearchException extends Exception {

	public IRISSearchException(String failedMethod, Throwable cause) {
		super("Call to '" + failedMethod + "' failed", cause);
	}

	public IRISSearchException(Throwable cause) {
		super(cause);
	}
}
