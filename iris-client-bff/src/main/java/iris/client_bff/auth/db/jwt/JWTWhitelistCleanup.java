package iris.client_bff.auth.db.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Configuration
@EnableScheduling
public class JWTWhitelistCleanup {

	private final JWTService jwtService;
	private final long DELETION_RATE = 30*60*1000; // 30 minutes

	public JWTWhitelistCleanup(JWTService jwtService) {
		this.jwtService = jwtService;
	}

	@Scheduled(fixedDelay = DELETION_RATE)
	public void clean() {
		jwtService.removeExpiredTokens();
	}

}
