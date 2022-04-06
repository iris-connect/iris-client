package iris.client_bff.auth.db.jwt;

import java.time.Instant;

import com.auth0.jwt.JWTCreator.Builder;

public interface JWTSigner {

	String sign(Builder builder);

	void saveToken(String jwtTokenDigest, String userName, Instant expirationTime);
}
