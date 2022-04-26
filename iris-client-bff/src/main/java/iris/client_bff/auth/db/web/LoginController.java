package iris.client_bff.auth.db.web;

import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.auth.db.login_attempts.LoginAttemptsService;
import iris.client_bff.core.log.LogHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

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
public class LoginController {

	public static final String AUTHENTICATION_INFO = "Authentication-Info";
	public static final String BEARER_TOKEN_PREFIX = "Bearer ";

	private final AuthenticationManager authManager;
	private final LoginAttemptsService loginAttempts;
	private final JWTService jwtService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto login, HttpServletRequest req)
			throws StreamReadException, DatabindException, IOException {

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

		var token = jwtService.createToken(user);

		var headers = new HttpHeaders();
		headers.add(AUTHENTICATION_INFO, BEARER_TOKEN_PREFIX + token);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, AUTHENTICATION_INFO);

		return ResponseEntity.ok().headers(headers).body(new LoginResponseDto(token));
	}

	// @ExceptionHandler
	// ResponseEntity<?> heandleLockedException(LockedException e) {
	// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	// }

	static record LoginRequestDto(@NotBlank String userName, @NotBlank String password) {}

	static record LoginResponseDto(String token) {}
}
