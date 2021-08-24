package iris.client_bff.auth.db.login_attempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private LoginAttemptsService loginAttemptService;

	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent e) {
		loginAttemptService.loginSucceeded(e.getAuthentication().getName());
	}
}
