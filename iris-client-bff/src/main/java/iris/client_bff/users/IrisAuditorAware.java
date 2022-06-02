package iris.client_bff.users;

import iris.client_bff.users.UserAccount.UserAccountIdentifier;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class IrisAuditorAware implements AuditorAware<UUID> {

	private final AuthenticatedUserAware authManager;

	@Override
	public Optional<UUID> getCurrentAuditor() {

		return authManager.getCurrentUser()
				.map(UserAccount::getId)
				.map(UserAccountIdentifier::toUuid);
	}
}
