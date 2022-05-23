package iris.client_bff.auth.db.web;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.*;
import static org.springframework.security.core.authority.AuthorityUtils.*;

import iris.client_bff.auth.db.AuthenticationStatus;
import iris.client_bff.auth.db.MfAuthenticationProperties;
import iris.client_bff.auth.db.MfAuthenticationProperties.MfAuthenticationOptions;
import iris.client_bff.auth.db.OtpAuthenticationToken;
import iris.client_bff.auth.db.RefreshTokenException;
import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.auth.db.login_attempts.LoginAttemptsService;
import iris.client_bff.core.log.LogHelper;
import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jens Kutzsche
 */
@RestController
@RequestMapping
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

	private final AuthenticationManager authManager;
	private final LoginAttemptsService loginAttempts;
	private final JWTService jwtService;
	private final UserService userService;
	private final MessageSourceAccessor messages;
	private final MfAuthenticationProperties mfaProperties;

	@PostMapping("/login")
	ResponseEntity<ResponseDto> login(@RequestBody @Valid LoginDto login, HttpServletRequest req) {

		log.debug("Login request from remote address: " + LogHelper.obfuscateLastThree(req.getRemoteAddr()));

		var userName = login.userName();

		checkPreviousLoginAttempts(userName);
		var user = authenticate(login, req);

		if (!useMfa(user)) {
			return createAuthenticatedResponse(userName);
		}

		if (user.isMfaSecretEnrolled()) {
			return createPreAuthResponse(userName, AuthenticationStatus.PRE_AUTHENTICATED_MFA_REQUIRED, null,
					null);
		}

		// Enables MFA if this is not already active. This could be the case if `security.auth.db.mfa.option` = `always`
		// is set.
		userService.updateUseMfa(user, true);
		// Reset the secret to avoid that someone gets the secret and this goes unnoticed because the enrollment is not
		// completed.
		userService.resetMfaSecret(user);

		return createPreAuthResponse(userName, AuthenticationStatus.PRE_AUTHENTICATED_ENROLLMENT_REQUIRED,
				userService.generateQrCodeImageUri(user), user.getMfaSecret());
	}

	@PostMapping("/mfa/otp")
	ResponseEntity<ResponseDto> verifyOtp(@RequestBody @Valid MfaDto mfaRequest, Principal principal) {

		var authenticationToken = new OtpAuthenticationToken(principal.getName(), mfaRequest.otp());
		authManager.authenticate(authenticationToken);

		return createAuthenticatedResponse(principal.getName());
	}

	@GetMapping(value = "/refreshtoken")
	ResponseEntity<?> refreshtoken(HttpServletRequest req) {

		log.debug("Refresh token request from remote address: " + LogHelper.obfuscateLastThree(req.getRemoteAddr()));

		var username = jwtService.getTokenFromCookie(req)
				.toEither("AuthenticationController.missing_jwt")
				.filterOrElse(it -> it.getExpiresAt().isBefore(Instant.now()),
						__ -> "AuthenticationController.not_expired_jwt")
				.map(Jwt::getSubject)
				.mapLeft(messages::getMessage)
				.getOrElseThrow(this::createException);

		var jwtCookie = jwtService.getTokenFromRefreshCookie(req)
				.toEither("AuthenticationController.missing_refresh_jwt")
				.map(Jwt::getSubject)
				.filterOrElse(username::equals, __ -> "AuthenticationController.subjects_dont_match")
				.map(jwtService::createJwtCookie)
				.mapLeft(messages::getMessage)
				.getOrElseThrow(this::createException);

		return ResponseEntity.noContent()
				.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.build();
	}

	private void checkPreviousLoginAttempts(String userName) {

		loginAttempts.getBlockedUntil(userName)
				.ifPresent(it -> {
					throw new LockedException(String.format("User blocked! (%s)", it));
				});
	}

	private UserAccount authenticate(LoginDto login, HttpServletRequest req) {

		var authToken = new UsernamePasswordAuthenticationToken(login.userName(), login.password());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

		var auth = authManager.authenticate(authToken);

		var user = userService.findByUsername(auth.getName()).get(); // must exist at this point

		SecurityContextHolder.getContext().setAuthentication(
				authenticated(user, null, createAuthorityList(user.getRole().name())));

		return user;
	}

	private boolean useMfa(UserAccount user) {
		return mfaProperties.getOption() == MfAuthenticationOptions.ALWAYS ||
				(mfaProperties.isMfaEnabled() && user.usesMfa());
	}

	private ResponseEntity<ResponseDto> createPreAuthResponse(String userName, AuthenticationStatus authStatus,
			String qrCodeImageUri, String mfaSecret) {

		String[] cookies = { jwtService.createPreAuthJwtCookie(userName, authStatus).toString() };

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookies)
				.body(new ResponseDto(authStatus, qrCodeImageUri, mfaSecret));
	}

	ResponseEntity<ResponseDto> createAuthenticatedResponse(String userName) {

		String[] cookies = { jwtService.createJwtCookie(userName).toString(),
				jwtService.createRefreshCookie(userName).toString() };

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookies)
				.body(new ResponseDto(AuthenticationStatus.AUTHENTICATED, null, null));
	}

	private RuntimeException createException(String reason) {
		return new RefreshTokenException(reason);
	}

	static record LoginDto(@NotBlank String userName, @NotBlank String password) {}

	static record MfaDto(@NotBlank String otp) {}

	static record ResponseDto(AuthenticationStatus authenticationStatus, String qrCodeImageUri,
			String mfaSecret) {}
}
