package iris.client_bff.auth.db;

import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.users.UserAccount.UserLoginIncompatiblyUpdated;
import iris.client_bff.users.UserAccount.UserMarkedDeleted;
import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
class UserEventListener {

	private final JWTService jwtService;

	@EventListener
	void on(UserMarkedDeleted event) {
		invalidateTokens(event.getUserName());
	}

	@EventListener
	void on(UserLoginIncompatiblyUpdated event) {
		invalidateTokens(event.getUserName());
	}

	private void invalidateTokens(String userName) {
		jwtService.invalidateTokensOfUser(userName);
	}
}
