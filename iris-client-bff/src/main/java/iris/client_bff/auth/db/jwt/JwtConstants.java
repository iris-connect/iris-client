package iris.client_bff.auth.db.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class JwtConstants {

	public static final Duration EXPIRATION_TIME = Duration.ofHours(1);
	public static final String JWT_CLAIM_USER_ROLE = "role";
}
