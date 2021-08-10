package iris.client_bff.auth.db.login_attempts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Slf4j
@Service()
@RequiredArgsConstructor
class Cleaner {

	private final @NonNull LoginAttemptsRepository loginAttempts;
	private final @NonNull LoginAttemptsProperties properties;

	@Scheduled(cron = "${security.login.attempts.cleaner-cron:-}")
	public void clean() {

		log.trace("Login Attemts Cleaner - start cleaning");

		var time = Instant.now().minus(properties.getIgnoreOldAttemptsAfter());
		loginAttempts.deleteBylastModifiedBefore(time);

		log.trace("Login Attemts Cleaner - cleaning finished");
	}
}
