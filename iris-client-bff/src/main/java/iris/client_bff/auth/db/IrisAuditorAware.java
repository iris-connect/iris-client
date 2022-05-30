package iris.client_bff.auth.db;

import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class IrisAuditorAware implements AuditorAware<UUID> {

	@Override
	public Optional<UUID> getCurrentAuditor() {

		return Optional.ofNullable(SecurityContextHolder.getContext()
				.getAuthentication())
				.map(Authentication::getDetails)
				.filter(UserAccount.class::isInstance)
				.map(UserAccount.class::cast)
				.map(UserAccount::getId)
				.map(UserAccountIdentifier::toUuid);
	}
}
