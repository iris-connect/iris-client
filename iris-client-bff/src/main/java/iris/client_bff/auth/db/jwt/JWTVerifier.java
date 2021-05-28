package iris.client_bff.auth.db.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface JWTVerifier {

	DecodedJWT verify(String jwt);

	boolean isTokenWhitelisted(String jwtTokenDigest);
}
