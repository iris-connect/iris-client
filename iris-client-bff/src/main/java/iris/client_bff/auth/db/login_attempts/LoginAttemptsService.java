package iris.client_bff.auth.db.login_attempts;

import static java.time.Instant.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import javax.validation.constraints.Positive;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
@Slf4j
public class LoginAttemptsService {

	private final @NonNull LoginAttemptsRepository loginAttempts;
	private final @NonNull LoginAttemptsService.Properties properties;

	public void loginSucceeded(String key) {

		try {
			loginAttempts.deleteById(DigestUtils.md5Hex(key));
		} catch (EmptyResultDataAccessException e) {}
	}

	@Synchronized
	public void loginFailed(String key) {

		var keyHash = DigestUtils.md5Hex(key);

		var attempt = loginAttempts.findById(keyHash)
				.map(it -> it.setWaitingTime(it.getWaitingTime().multipliedBy(properties.getWaitingTimeMultiplier())))
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
					"For user name (hash: {}) there were {} failed attempts to log in to the IRIS client. The next warning occurs at {}.",
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

	/**
	 * @author Jens Kutzsche
	 */
	@ConfigurationProperties(prefix = "security.login.attempts")
	@ConstructorBinding
	@Validated
	@Value
	static class Properties {

		@Positive
		private int firstWarningThreshold;
		@Positive
		private int warningThresholdMultiplier;
		@DurationMin(seconds = 1)
		private Duration firstWaitingTime;
		@Positive
		private int waitingTimeMultiplier;
		@DurationMin(hours = 1)
		private Duration ignoreOldAttemptsAfter;
	}
}
