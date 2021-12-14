package iris.client_bff.status;

/**
 * @author Jens Kutzsche
 */
public class AppStatusException extends RuntimeException {

	private static final long serialVersionUID = 3580183974567793366L;

	public AppStatusException(String message) {
		super(message);
	}
}
