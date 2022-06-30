package iris.client_bff.auth.db.login_attempts;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private final LoginAttemptsService loginAttemptService;

	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent e) {
		loginAttemptService.loginSucceeded(e.getAuthentication().getName());
	}
}
