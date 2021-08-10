package iris.client_bff.auth.db;

import static iris.client_bff.auth.db.SecurityConstants.*;

import iris.client_bff.auth.db.dto.LoginRequestDTO;
import iris.client_bff.auth.db.jwt.JWTSigner;
import iris.client_bff.auth.db.login_attempts.LoginAttemptsRepository;
import iris.client_bff.core.log.LogHelper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

@AllArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTSigner jwtSigner;
	private final @NonNull LoginAttemptsRepository loginAttempts;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {

		log.debug("Login request from remote address: " + LogHelper.obfuscateLastThree(req.getRemoteAddr()));

		try {

			LoginRequestDTO loginRequest = new ObjectMapper()
					.readValue(req.getInputStream(), LoginRequestDTO.class);

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUserName(),
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

		User user = (User) auth.getPrincipal();

		// By convention we expect that there exists only one authority and it represents the role
		var role = user.getAuthorities().stream().findFirst().get().getAuthority();

		var expirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
		var token = jwtSigner.sign(JWT.create()
				.withSubject(user.getUsername())
				.withClaim(JWT_CLAIM_USER_ROLE, role)
				.withExpiresAt(expirationTime));

		// Whitelist the token
		jwtSigner.saveToken(token, user.getUsername(), expirationTime.toInstant());

		res.addHeader(AUTHENTICATION_INFO, BEARER_TOKEN_PREFIX + token);
		res.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, AUTHENTICATION_INFO);

		var remoteAddr = DigestUtils.md5Hex(req.getRemoteAddr());
		try {
			loginAttempts.deleteById(remoteAddr);
		} catch (EmptyResultDataAccessException e) {}
	}
}
