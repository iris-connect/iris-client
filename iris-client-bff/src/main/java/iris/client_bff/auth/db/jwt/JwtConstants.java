package iris.client_bff.auth.db.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class JwtConstants {

	public static final String JWT_CLAIM_USER_ROLE = "role";
}
