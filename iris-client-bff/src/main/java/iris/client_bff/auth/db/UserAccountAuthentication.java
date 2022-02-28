package iris.client_bff.auth.db;

import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;
import lombok.AllArgsConstructor;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class UserAccountAuthentication implements Authentication {

	private static final long serialVersionUID = 2483888828181651499L;

	private final UserAccount userAccount;

	private boolean authenticated;

	private final Collection<? extends GrantedAuthority> grantedAuthorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return userAccount;
	}

	@Override
	public Object getPrincipal() {
		return userAccount.getUserName();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean b) throws IllegalArgumentException {
		this.authenticated = b;
	}

	@Override
	public String getName() {
		return userAccount.getUserName();
	}

	public boolean isAdmin() {
		return this.getAuthorities()
				.stream()
				.anyMatch(auth -> auth.getAuthority().equals(UserRole.ADMIN.name()));
	}
}
