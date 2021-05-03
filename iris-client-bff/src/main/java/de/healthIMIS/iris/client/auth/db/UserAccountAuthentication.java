package de.healthIMIS.iris.client.auth.db;

import de.healthIMIS.iris.client.users.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
@Builder
public class UserAccountAuthentication implements Authentication {

	private String userName;

	private boolean authenticated;

	private Collection<? extends GrantedAuthority> grantedAuthorities;

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
		return userName;
	}

	@Override
	public Object getPrincipal() {
		return userName;
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
		return userName;
	}

	public boolean isAdmin() {
		return this.getAuthorities()
				.stream()
				.anyMatch(auth -> auth.getAuthority().equals(UserRole.ADMIN.name()));
	}

}
