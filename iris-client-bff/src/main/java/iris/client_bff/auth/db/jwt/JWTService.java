package iris.client_bff.auth.db.jwt;

import static iris.client_bff.auth.db.jwt.JwtConstants.*;
import static java.util.Optional.*;
import static org.apache.commons.lang3.StringUtils.*;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.WebUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;

@Service
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@Slf4j
public class JWTService {

	private static final String PATH = "/";
	private static final String REFRESH_PATH = "/refreshtoken";

	private final AllowedTokenRepository allowedTokenRepository;

	private final JWTService.Properties jwtProperties;

	private final JwtDecoder decoder;
	private final JwtDecoder laxDecoder;
	private final JwtDecoder refreshDecoder;

	public JWTService(AllowedTokenRepository allowedTokenRepository, Properties jwtProperties) {

		this.allowedTokenRepository = allowedTokenRepository;
		this.jwtProperties = jwtProperties;

		decoder = createJwtDecoder(jwtProperties.getSharedSecret(), createJwtValidator());
		laxDecoder = createJwtDecoder(jwtProperties.getSharedSecret(), this::isTokenWhitelisted);
		refreshDecoder = createJwtDecoder(jwtProperties.getRefresh().getSharedSecret(), createJwtValidator());
	}

	public BearerTokenResolver createTokenResolver() {

		return request -> of(request)
				.filter(it -> !endsWithAny(it.getRequestURI(), "/login", REFRESH_PATH))
				.map(it -> WebUtils.getCookie(it, jwtProperties.getCookieName()))
				.map(Cookie::getValue)
				.orElse(null);
	}

	public JwtDecoder getJwtDecoder() {
		return decoder;
	}

	public ResponseCookie createJwtCookie(UserDetails user) {

		var refreshExpirationTime = jwtProperties.getRefresh().getExpirationTime();
		var jwt = createToken(user, jwtProperties.getExpirationTime(), refreshExpirationTime, this::signToken);

		return ResponseCookie.from(jwtProperties.getCookieName(), jwt)
				.maxAge(refreshExpirationTime.toSeconds())
				.secure(jwtProperties.isSetSecure())
				.path(PATH)
				.httpOnly(true)
				.sameSite(jwtProperties.getSameSiteStr())
				.build();
	}

	public ResponseCookie createCleanJwtCookie() {
		// Without setting maxAge >= 0, we would create a session cookie.
		// Setting it to 0 will delete the cookie.
		return ResponseCookie.from(jwtProperties.getCookieName(), null).maxAge(0).path(PATH).build();
	}

	public ResponseCookie createRefreshCookie(UserDetails user) {

		var refresh = jwtProperties.getRefresh();
		var expirationTime = refresh.getExpirationTime();
		var jwt = createToken(user, expirationTime, expirationTime, this::signRefreshToken);

		return ResponseCookie.from(refresh.getCookieName(), jwt)
				.maxAge(expirationTime.toSeconds())
				.secure(jwtProperties.isSetSecure())
				.path(REFRESH_PATH)
				.httpOnly(true)
				.sameSite(jwtProperties.getSameSiteStr())
				.build();
	}

	public ResponseCookie createCleanRefreshCookie() {
		// Without setting maxAge >= 0, we would create a session cookie.
		// Setting it to 0 will delete the cookie.
		return ResponseCookie.from(jwtProperties.getRefresh().getCookieName(), null).maxAge(0).path(REFRESH_PATH).build();
	}

	public Try<Jwt> getTokenFromCookie(HttpServletRequest request) {

		return Option.of(WebUtils.getCookie(request, jwtProperties.getCookieName()))
				.map(Cookie::getValue)
				.toTry()
				.map(laxDecoder::decode);
	}

	public Try<Jwt> getTokenFromRefreshCookie(HttpServletRequest request) {

		return Option.of(WebUtils.getCookie(request, jwtProperties.getRefresh().getCookieName()))
				.map(Cookie::getValue)
				.toTry()
				.map(refreshDecoder::decode);
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

	private JwtDecoder createJwtDecoder(String sharedSecret, OAuth2TokenValidator<Jwt> validator) {

		var algorithmName = getAlgorithm(sharedSecret).getName();
		var key = new SecretKeySpec(sharedSecret.getBytes(StandardCharsets.UTF_8), algorithmName);

		var decoder = NimbusJwtDecoder.withSecretKey(key)
				.macAlgorithm(MacAlgorithm.from(algorithmName))
				.build();

		decoder.setJwtValidator(validator);

		return decoder;
	}

	private OAuth2TokenValidator<Jwt> createJwtValidator() {

		var timestampValidator = new JwtTimestampValidator(Duration.ZERO);

		List<OAuth2TokenValidator<Jwt>> validators = List.of(
				timestampValidator,
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

	private String createToken(UserDetails user, Duration tokenExpirationDuration, Duration entityExpirationDuration,
			Function<Builder, String> signFunction) {

		var username = user.getUsername();

		// By convention we expect that there exists only one authority and it represents the role
		var role = user.getAuthorities().iterator().next().getAuthority();
		var issuedAt = Instant.now();

		var token = signFunction.apply(JWT.create()
				.withSubject(username)
				.withClaim(JWT_CLAIM_USER_ROLE, role)
				.withIssuedAt(Date.from(issuedAt))
				.withExpiresAt(Date.from(issuedAt.plus(tokenExpirationDuration))));

		saveToken(token, username, issuedAt.plus(entityExpirationDuration), issuedAt);

		return token;
	}

	private String signToken(Builder builder) {
		return builder.sign(getAlgorithm(jwtProperties.getSharedSecret()));
	}

	private String signRefreshToken(Builder builder) {
		return builder.sign(getAlgorithm(jwtProperties.getRefresh().getSharedSecret()));
	}

	private Algorithm getAlgorithm(String sharedSecret) {
		return Algorithm.HMAC512(sharedSecret);
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

		@NotBlank
		String sharedSecret;

		@NotNull
		@DurationMin(seconds = 1)
		Duration expirationTime;

		@NotBlank
		String cookieName;

		boolean setSecure;

		SameSite sameSite;

		String getSameSiteStr() {
			return getSameSite().name();
		}

		@NotNull
		@Valid
		RefreshProperties refresh;
	}

	@Value
	static class RefreshProperties {

		@NotBlank
		String sharedSecret;

		@NotNull
		@DurationMin(seconds = 1)
		Duration expirationTime;

		@NotBlank
		String cookieName;
	}

	enum SameSite {
		Strict, Lax
	}
}
