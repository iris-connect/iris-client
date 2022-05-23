package iris.client_bff.auth.db.jwt;

import static iris.client_bff.auth.db.jwt.JwtConstants.*;
import static java.util.Optional.*;
import static org.apache.commons.lang3.StringUtils.*;

import io.vavr.control.Option;
import io.vavr.control.Try;
import iris.client_bff.auth.db.AuthenticationStatus;
import iris.client_bff.core.setting.Setting;
import iris.client_bff.core.setting.Setting.Name;
import iris.client_bff.core.setting.SettingsRepository;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.http.ResponseCookie;
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

	public ResponseCookie createJwtCookie(String username) {

		return createJwtCookie(new JwtCookieData(username, null, jwtProperties.getCookieName(),
				jwtProperties.getExpirationTime(), jwtProperties.getRefresh().getExpirationTime(), jwtProperties.isSetSecure(),
				PATH, jwtProperties.getSameSiteStr(), this::signToken));
	}

	public ResponseCookie createPreAuthJwtCookie(String username, AuthenticationStatus authStatus) {

		var expirationTime = Duration.ofMinutes(5);
		return createJwtCookie(new JwtCookieData(username, authStatus, jwtProperties.getCookieName(), expirationTime,
				expirationTime, jwtProperties.isSetSecure(), PATH, jwtProperties.getSameSiteStr(), this::signToken));
	}

	public ResponseCookie createCleanJwtCookie() {
		// Without setting maxAge >= 0, we would create a session cookie.
		// Setting it to 0 will delete the cookie.
		return ResponseCookie.from(jwtProperties.getCookieName(), null).maxAge(0).path(PATH).build();
	}

	public ResponseCookie createRefreshCookie(String username) {

		var refresh = jwtProperties.getRefresh();
		var expirationTime = refresh.getExpirationTime();

		return createJwtCookie(new JwtCookieData(username, null, refresh.getCookieName(), expirationTime, expirationTime,
				jwtProperties.isSetSecure(), REFRESH_PATH, jwtProperties.getSameSiteStr(), this::signRefreshToken));
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

	private ResponseCookie createJwtCookie(JwtCookieData data) {

		var jwt = createToken(data.username(), data.authStatus, data.tokenExpirationDuration(),
				data.cookieExpirationDuration(), data.signFunction());

		return ResponseCookie.from(data.cookieName(), jwt)
				.maxAge(data.cookieExpirationDuration().toSeconds())
				.secure(data.secure())
				.path(data.path())
				.httpOnly(true)
				.sameSite(data.sameSite())
				.build();
	}

	private String createToken(String username, AuthenticationStatus authStatus, Duration tokenExpirationDuration,
			Duration entityExpirationDuration, Function<Builder, String> signFunction) {

		var issuedAt = Instant.now();

		var jwtBuilder = JWT.create()
				.withSubject(username)
				.withIssuedAt(Date.from(issuedAt))
				.withExpiresAt(Date.from(issuedAt.plus(tokenExpirationDuration)));

		if (authStatus != null) {
			jwtBuilder = jwtBuilder.withClaim(JWT_CLAIM_AUTH_STATUS, authStatus.toString());
		}

		var token = signFunction.apply(jwtBuilder);

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

		private static final Random random = new SecureRandom();

		@Autowired
		SettingsRepository settings;

		@Nullable
		@NonFinal
		String sharedSecret;

		@NotNull
		Period saveSecretFor;

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

		@PostConstruct
		void initSecrets() {

			if (isBlank(sharedSecret)) {

				var setting = settings.findById(Name.JWT_SECRET)
						.filter(it -> it.getSavedAt().plus(saveSecretFor).isAfter(LocalDate.now()))
						.orElseGet(() -> createNewRandomSecret(Name.JWT_SECRET));

				sharedSecret = setting.getStoredValue();
			}

			if (isBlank(refresh.getSharedSecret())) {

				var setting = settings.findById(Name.REFRESH_SECRET)
						.filter(it -> it.getSavedAt().plus(refresh.getSaveSecretFor()).isAfter(LocalDate.now()))
						.orElseGet(() -> createNewRandomSecret(Name.REFRESH_SECRET));

				refresh.sharedSecret = setting.getStoredValue();
			}
		}

		private Setting createNewRandomSecret(Name name) {

			// 64 Bytes of printable Ascii characters
			var secret = RandomStringUtils.random(64, 32, 127, false, false, null, random);

			return settings.save(new Setting(name, secret, LocalDate.now()));
		}
	}

	@Value
	static class RefreshProperties {

		@Nullable
		@NonFinal
		String sharedSecret;

		@NotNull
		Period saveSecretFor;

		@NotNull
		@DurationMin(seconds = 1)
		Duration expirationTime;

		@NotBlank
		String cookieName;
	}

	enum SameSite {
		Strict, Lax
	}

	static record JwtCookieData(String username,
			AuthenticationStatus authStatus,
			String cookieName,
			Duration tokenExpirationDuration,
			Duration cookieExpirationDuration,
			boolean secure,
			String path,
			String sameSite,
			Function<Builder, String> signFunction) {}
}
