package iris.client_bff.auth.db;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Jens Kutzsche
 */
public class RefreshTokenException extends AuthenticationException {

	private static final long serialVersionUID = 6158791145978867925L;

	/**
	 * @param msg
	 */
	public RefreshTokenException(String msg) {
		super(msg);
	}
}
