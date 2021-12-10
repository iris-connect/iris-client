package iris.client_bff.status;

/**
 * @author Jens Kutzsche
 */
public class EpsConnectionException extends AppStatusException {

	private static final long serialVersionUID = 5854436436213842804L;

	public EpsConnectionException(String message) {
		super(message);
	}
}
