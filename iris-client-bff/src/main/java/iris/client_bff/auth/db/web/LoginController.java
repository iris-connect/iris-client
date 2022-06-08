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

	private final AuthenticationManager authManager;
	private final LoginAttemptsService loginAttempts;
	private final JWTService jwtService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto login, HttpServletRequest req) throws IOException {

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

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).build();
	}

	static record LoginRequestDto(@NotBlank String userName, @NotBlank String password) {}

	static record LoginResponseDto(String token) {}
}
