package iris.client_bff.auth.db;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

	public static final Duration EXPIRATION_TIME = Duration.ofHours(1);

	public static final String BEARER_TOKEN_PREFIX = "Bearer ";
	public static final String AUTHENTICATION_INFO = "Authentication-Info";
	public static final String JWT_CLAIM_USER_ROLE = "role";
}
