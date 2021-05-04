package iris.client_bff.auth.db.jwt;

import lombok.AllArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@AllArgsConstructor
@Component
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
public class JWTService implements JWTVerifier, JWTSigner {

	private JwtProperties jwtProperties;

	@Override
	public String sign(Builder builder) {
		return builder.sign(getAlgorithm());
	}

	@Override
	public DecodedJWT verify(String jwt) {
		return JWT.require(getAlgorithm()).build().verify(jwt);
	}

	private Algorithm getAlgorithm() {
		return Algorithm.HMAC512(jwtProperties.getJwtSharedSecret());
	}

}
