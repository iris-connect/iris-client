package iris.client_bff.auth.db;

import lombok.EqualsAndHashCode;
import lombok.Value;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * @author Jens Kutzsche
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class OtpAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private Object principal;
	private Object credentials;

	public OtpAuthenticationToken(Object principal, Object credentials) {

		super(null);

		this.principal = principal;
		this.credentials = credentials;
	}
}
