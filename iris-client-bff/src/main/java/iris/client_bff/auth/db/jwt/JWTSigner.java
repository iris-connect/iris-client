package iris.client_bff.auth.db.jwt;

import com.auth0.jwt.JWTCreator.Builder;

import java.time.Instant;

public interface JWTSigner {

	String sign(Builder builder);

	void saveToken(String jwtTokenDigest, String userName, Instant expirationTime);
}
