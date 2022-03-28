package iris.client_bff.auth.db.jwt;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Configuration
@AllArgsConstructor
public class JWTWhitelistCleanup {

	private final JWTService jwtService;
	private final long DELETION_RATE = 30 * 60 * 1000; // 30 minutes

	@Scheduled(fixedDelay = DELETION_RATE)
	public void clean() {
		jwtService.removeExpiredTokens();
	}

}
