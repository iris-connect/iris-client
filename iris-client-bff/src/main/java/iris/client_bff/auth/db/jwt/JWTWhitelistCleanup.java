package iris.client_bff.auth.db.jwt;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
class JWTWhitelistCleanup {

	private static final long DELETION_RATE = 30 * 60 * 1000l; // 30 minutes

	private final JWTService jwtService;

	@Scheduled(fixedDelay = DELETION_RATE)
	public void clean() {
		jwtService.removeExpiredTokens();
	}
}
