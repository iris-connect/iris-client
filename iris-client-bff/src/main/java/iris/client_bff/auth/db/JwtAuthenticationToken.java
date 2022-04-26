package iris.client_bff.auth.db;

import static iris.client_bff.users.UserRole.*;

import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserRole;
import lombok.NonNull;

import java.util.EnumSet;
import java.util.Map;

import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

@Transient
public class JwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

	private static final long serialVersionUID = -2363012036109039609L;

	private static final EnumSet<UserRole> AUTHENTICATION_ROLES = EnumSet.of(ADMIN, USER);

	private final String name;

	public JwtAuthenticationToken(@NonNull Jwt jwt, @NonNull UserAccount principal) {

		// By convention we expect that there exists only one authority and it represents the role
		super(jwt, principal, null,
				AuthorityUtils.createAuthorityList(principal.getRole().name()));

		this.setAuthenticated(isAuthenticated(principal));
		this.name = jwt.getSubject();
	}

	@Override
	public Map<String, Object> getTokenAttributes() {
		return this.getToken().getClaims();
	}

	@Override
	public String getName() {
		return this.name;
	}

	private boolean isAuthenticated(@NonNull UserAccount principal) {
		return AUTHENTICATION_ROLES.contains(principal.getRole());
	}
}
