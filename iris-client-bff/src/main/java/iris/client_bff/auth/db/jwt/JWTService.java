package iris.client_bff.auth.db.jwt;

import static iris.client_bff.auth.db.jwt.JwtConstants.*;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotBlank;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;

@Service
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
@Slf4j
public class JWTService {

	private final AllowedTokenRepository allowedTokenRepository;

	private final JWTService.Properties jwtProperties;

	public JwtDecoder createJwtDecoder() {

		var algorithmName = getAlgorithm().getName();
		var key = new SecretKeySpec(jwtProperties.getSharedSecret().getBytes(StandardCharsets.UTF_8), algorithmName);

		var decoder = NimbusJwtDecoder.withSecretKey(key)
				.macAlgorithm(MacAlgorithm.from(algorithmName))
				.build();

		decoder.setJwtValidator(createJwtValidator());

		return decoder;
	}

	public String createToken(UserDetails user) {

		var username = user.getUsername();

		// By convention we expect that there exists only one authority and it represents the role
		var role = user.getAuthorities().iterator().next().getAuthority();

		var issuedAt = Instant.now();
		var expirationTime = issuedAt.plus(EXPIRATION_TIME);

		var token = sign(JWT.create()
				.withSubject(username)
				.withClaim(JWT_CLAIM_USER_ROLE, role)
				.withIssuedAt(Date.from(issuedAt))
				.withExpiresAt(Date.from(expirationTime)));

		saveToken(token, username, expirationTime, issuedAt);

		return token;
	}

	public boolean isTokenWhitelisted(String token) {

		return allowedTokenRepository
				.findByJwtTokenDigest(hashToken(token))
				.isPresent();
	}

	public void invalidateTokensOfUser(String userName) {
		allowedTokenRepository.deleteByUserName(userName);
	}

	public void removeExpiredTokens() {
		allowedTokenRepository.deleteByExpirationTimeBefore(Instant.now());
	}

	private OAuth2TokenValidator<Jwt> createJwtValidator() {

		List<OAuth2TokenValidator<Jwt>> validators = List.of(
				JwtValidators.createDefault(),
				this::isTokenWhitelisted);

		return new DelegatingOAuth2TokenValidator<>(validators);
	}

	private OAuth2TokenValidatorResult isTokenWhitelisted(Jwt jwt) {

		if (isTokenWhitelisted(jwt.getTokenValue())) {
			return OAuth2TokenValidatorResult.success();
		}

		var error = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, "The token is not whitelisted", null);
		log.debug(error.getDescription());
		return OAuth2TokenValidatorResult.failure(error);
	}

	private String sign(Builder builder) {
		return builder.sign(getAlgorithm());
	}

	private Algorithm getAlgorithm() {
		return Algorithm.HMAC512(jwtProperties.getSharedSecret());
	}

	private AllowedToken saveToken(String token, String userName, Instant expirationTime, Instant created) {
		return allowedTokenRepository.save(AllowedToken.of(hashToken(token), userName, expirationTime, created));
	}

	private String hashToken(String jwt) {
		return DigestUtils.sha256Hex(jwt);
	}

	@ConfigurationProperties(prefix = "security.jwt")
	@ConstructorBinding
	@ConditionalOnProperty(
			value = "security.auth",
			havingValue = "db")
	@Validated
	@Value
	static class Properties {

		private @NotBlank String sharedSecret;
	}
}
