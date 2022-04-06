package iris.client_bff.auth.db;

import static iris.client_bff.auth.db.SecurityConstants.*;

import iris.client_bff.auth.db.dto.LoginRequestDTO;
import iris.client_bff.auth.db.jwt.JWTSigner;
import iris.client_bff.auth.db.login_attempts.LoginAttemptsService;
import iris.client_bff.auth.db.login_attempts.WaitingTimeAuthenticationFailureHandler;
import iris.client_bff.core.log.LogHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTSigner jwtSigner;
	private final LoginAttemptsService loginAttempts;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTSigner jwtSigner,
			LoginAttemptsService loginAttempts) {

		this.authenticationManager = authenticationManager;
		this.jwtSigner = jwtSigner;
		this.loginAttempts = loginAttempts;

		setAuthenticationFailureHandler(new WaitingTimeAuthenticationFailureHandler());
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {

		log.debug("Login request from remote address: " + LogHelper.obfuscateLastThree(req.getRemoteAddr()));

		try {

			var loginRequest = new ObjectMapper()
					.readValue(req.getInputStream(), LoginRequestDTO.class);
			var userName = loginRequest.getUserName();

			loginAttempts.getBlockedUntil(userName)
					.ifPresent(it -> {
						throw new LockedException(String.format("User blocked! (%s)", it));
					});

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							userName,
							loginRequest.getPassword(),
							new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This Method is called after successfull authentication. It is responsible for placing the user role in the jwt
	 * token.
	 *
	 * @param auth Contains user principle with user information
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		var user = (User) auth.getPrincipal();

		// By convention we expect that there exists only one authority and it represents the role
		var role = user.getAuthorities().stream().findFirst().get().getAuthority();

		var expirationTime = Instant.now().plus(EXPIRATION_TIME);
		var token = jwtSigner.sign(JWT.create()
				.withSubject(user.getUsername())
				.withClaim(JWT_CLAIM_USER_ROLE, role)
				.withExpiresAt(Date.from(expirationTime)));

		// Whitelist the token
		jwtSigner.saveToken(token, user.getUsername(), expirationTime);

		res.addHeader(AUTHENTICATION_INFO, BEARER_TOKEN_PREFIX + token);
		res.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, AUTHENTICATION_INFO);
	}
}
