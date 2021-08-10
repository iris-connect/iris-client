package iris.client_bff.auth.db.login_attempts;

import static java.time.Instant.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginAttemptsService {

	private final @NonNull LoginAttemptsRepository loginAttempts;
	private final @NonNull LoginAttemptsProperties properties;

	public void loginSucceeded(String key) {

		try {
			loginAttempts.deleteById(DigestUtils.md5Hex(key));
		} catch (EmptyResultDataAccessException e) {}
	}

	public void loginFailed(String key) {

		var keyHash = DigestUtils.md5Hex(key);

		var attempt = loginAttempts.findById(keyHash)
				.map(it -> {
					it.setWaitingTime(it.getWaitingTime().multipliedBy(properties.getWaitingTimeMultiplier()));
					return it;
				})
				.orElseGet(() -> LoginAttempts.builder()
						.reference(keyHash)
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
					keyHash, attempts, newNextWarningThreshold);
		}

		loginAttempts.save(attempt);
	}

	public Optional<Instant> getBlockedUntil(String key) {

		var now = now();

		return loginAttempts.findById(DigestUtils.md5Hex(key))
				.filter(it -> now.isBefore(it.getLastModified().plus(it.getWaitingTime())))
				.filter(it -> now.isBefore(it.getLastModified().plus(properties.getIgnoreOldAttemptsAfter())))
				.map(it -> it.getLastModified().plus(it.getWaitingTime()));
	}

	@Scheduled(cron = "${security.login.attempts.cleaner-cron:-}")
	public void clean() {

		log.trace("Login Attemts Cleaner - start cleaning");

		var time = now().minus(properties.getIgnoreOldAttemptsAfter());
		loginAttempts.deleteBylastModifiedBefore(time);

		log.trace("Login Attemts Cleaner - cleaning finished");
	}
}
