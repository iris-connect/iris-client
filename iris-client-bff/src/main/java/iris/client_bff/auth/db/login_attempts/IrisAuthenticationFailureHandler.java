package iris.client_bff.auth.db.login_attempts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IrisAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final @NonNull LoginAttemptsRepository loginAttempts;
	private final @NonNull LoginAttemptsProperties properties;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		var remoteAddr = DigestUtils.md5Hex(request.getRemoteAddr());

		var attempt = loginAttempts.findById(remoteAddr)
				.map(it -> {
					it.setWaitingTime(it.getWaitingTime().multipliedBy(properties.getWaitingTimeMultiplier()));
					return it;
				})
				.orElseGet(() -> LoginAttempts.builder()
						.remoteAddr(remoteAddr)
						.nextWarningThreshold(properties.getFirstWarningThreshold())
						.waitingTime(properties.getFirstWaitingTime())
						.build());

		var attempts = attempt.getAttempts() + 1;
		attempt.setAttempts(attempts);

		var nextWarningThreshold = attempt.getNextWarningThreshold();
		if (attempts >= nextWarningThreshold) {

			var newNextWarningThreshold = nextWarningThreshold * properties.getWarningThresholdMultiplier();
			attempt.setNextWarningThreshold(newNextWarningThreshold);

			log.warn(
					"From one IP (hash: {}) there were {} failed attempts to log in to the IRIS client. The next warning occurs at {}.",
					remoteAddr, attempts, newNextWarningThreshold);
		}

		loginAttempts.save(attempt);

		super.onAuthenticationFailure(request, response, exception);
	}
}
