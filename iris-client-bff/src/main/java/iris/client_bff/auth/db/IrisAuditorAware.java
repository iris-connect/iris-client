package iris.client_bff.auth.db;

import iris.client_bff.users.entities.UserAccount;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class IrisAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {

		return Optional.ofNullable(SecurityContextHolder.getContext()
				.getAuthentication())
				.map(Authentication::getDetails)
				.filter(UserAccount.class::isInstance)
				.map(UserAccount.class::cast)
				.map(this::createFullName);
	}

	private String createFullName(UserAccount account) {
		return account.getFirstName() + " " + account.getLastName();
	}
}
