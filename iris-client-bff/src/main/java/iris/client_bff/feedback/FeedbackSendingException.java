package iris.client_bff.feedback;

/**
 * @author Jens Kutzsche
 */
public class FeedbackSendingException extends RuntimeException {

	private static final long serialVersionUID = -1550186873048234582L;

	public FeedbackSendingException(Throwable cause) {
		super(cause);
	}
}
