package iris.client_bff.auth.db.web;

import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.auth.db.login_attempts.LoginAttemptsService;
import iris.client_bff.core.log.LogHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
	private final UserDetailsService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto login, HttpServletRequest req) {

		log.debug("Login request from remote address: " + LogHelper.obfuscateLastThree(req.getRemoteAddr()));

		var userName = login.userName();

		loginAttempts.getBlockedUntil(userName)
				.ifPresent(it -> {
					throw new LockedException(String.format("User blocked! (%s)", it));
				});

		var authToken = new UsernamePasswordAuthenticationToken(
				userName,
				login.password());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

		var user = (UserDetails) authManager.authenticate(authToken).getPrincipal();

		var jwtCookie = jwtService.createJwtCookie(user);
		var refreshCookie = jwtService.createRefreshCookie(user);

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, jwtCookie.toString(), refreshCookie.toString())
				.build();
	}

	@GetMapping(value = "/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest req) {

		log.debug("Refresh token request from remote address: " + LogHelper.obfuscateLastThree(req.getRemoteAddr()));

		var username = jwtService.getTokenFromCookie(req)
				.toEither("There isn't a usable JWT, but this is required for refresh the token.")
				.filterOrElse(it -> it.getExpiresAt().isBefore(Instant.now()),
						__ -> "The JWT isn't expired and you can't refresh a not expired token.")
				.map(Jwt::getSubject)
				.getOrElseThrow(it -> new ResponseStatusException(HttpStatus.BAD_REQUEST, it));

		var jwtCookie = jwtService.getTokenFromRefreshCookie(req)
				.toEither("A usable Refresh JWT is missing!")
				.map(Jwt::getSubject)
				.filterOrElse(username::equals, __ -> "Subjects of JWT and Refresh JWT don't match!")
				.map(userService::loadUserByUsername)
				.map(jwtService::createJwtCookie)
				.getOrElseThrow(it -> new ResponseStatusException(HttpStatus.BAD_REQUEST, it));

		return ResponseEntity.noContent()
				.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.build();
	}

	static record LoginRequestDto(@NotBlank String userName, @NotBlank String password) {}

	static record LoginResponseDto(String token) {}
}
