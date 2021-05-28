package iris.client_bff.auth.db.jwt;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
@AllArgsConstructor
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
public class JWTService implements JWTVerifier, JWTSigner {

	private final HashedTokenRepository hashedTokenRepository;

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

	@Override
	public void saveToken(String token, String userName, Instant expirationTime) {
		var hashedToken = new HashedToken();
		hashedToken.setJwtTokenDigest(hashToken(token));
		hashedToken.setUserName(userName);
		hashedToken.setExpirationTime(expirationTime);
		hashedTokenRepository.save(hashedToken);
	}

	@Override
	public boolean isTokenWhitelisted(String token) {
		Optional<HashedToken> hashedToken = hashedTokenRepository.findByJwtTokenDigest(hashToken(token));
		return hashedToken.isPresent();
	}

	public void invalidateTokensOfUser(String userName) {
		hashedTokenRepository.deleteByUserName(userName);
	}

	public void removeExpiredTokens() {
		hashedTokenRepository.deleteByExpirationTimeBefore(Instant.now());
	}

	private String hashToken(String jwt) {
		return DigestUtils.sha256Hex(jwt);
	}
}
