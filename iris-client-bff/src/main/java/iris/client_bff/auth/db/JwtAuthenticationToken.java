package iris.client_bff.auth.db;

import static java.util.Optional.*;
import static org.apache.commons.collections4.CollectionUtils.*;

import iris.client_bff.auth.db.jwt.JwtConstants;
import iris.client_bff.users.UserAccount;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

@Transient
@EqualsAndHashCode(callSuper = true)
public class JwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

	private static final long serialVersionUID = -2363012036109039609L;

	private final String name;

	public JwtAuthenticationToken(@NonNull Jwt jwt, @NonNull UserAccount principal) {

		super(jwt, principal, null, determineAuthority(jwt, principal));

		this.setAuthenticated(isNotEmpty(getAuthorities()));
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

	private static List<GrantedAuthority> determineAuthority(Jwt jwt, UserAccount principal) {
		return ofNullable(jwt.getClaimAsString(JwtConstants.JWT_CLAIM_AUTH_STATUS))
				.map(AuthorityUtils::createAuthorityList)
				// By convention we expect that there exists only one authority and it represents the role
				.orElseGet(() -> AuthorityUtils.createAuthorityList(principal.getRole().name()));
	}
}
