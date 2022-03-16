package iris.client_bff.vaccination_info.eps;

/**
 * @author Jens Kutzsche
 */
public class InvalidPublicKeyException extends RuntimeException {

	private static final long serialVersionUID = -62922796972802753L;

	public InvalidPublicKeyException(String msg, Throwable e) {
		super(msg, e);
	}
}
