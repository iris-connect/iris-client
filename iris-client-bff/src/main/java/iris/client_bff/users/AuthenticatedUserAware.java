package iris.client_bff.users;

import static iris.client_bff.users.UserRole.*;

import lombok.NonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserAware {

	public Optional<UserAccount> getCurrentUser() {

		return Optional.of(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.map(Authentication::getPrincipal)
				.filter(UserAccount.class::isInstance)
				.map(UserAccount.class::cast);
	}

	public boolean hasRole(@NonNull UserRole role) {
		return getCurrentAuthorities().anyMatch(role.name()::equals);
	}

	public boolean isAdmin() {
		return getCurrentAuthorities().anyMatch(ADMIN.name()::equals);
	}

	public boolean isUser() {
		return getCurrentAuthorities().anyMatch(USER.name()::equals);
	}

	public static Stream<UserRole> getCurrentRoles() {

		return getCurrentAuthorities()
				.filter(UserRole::isUserRole)
				.map(UserRole::valueOf);
	}

	private static Stream<String> getCurrentAuthorities() {

		return Stream.of(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.filter(Objects::nonNull)
				.flatMap(it -> it.getAuthorities().stream())
				.map(GrantedAuthority::getAuthority);
	}
}
