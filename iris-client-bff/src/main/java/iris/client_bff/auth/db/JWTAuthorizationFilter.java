package iris.client_bff.auth.db;

import static iris.client_bff.auth.db.SecurityConstants.*;

import iris.client_bff.auth.db.jwt.JWTVerifier;
import iris.client_bff.users.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private final JWTVerifier jwtVerifier;
	private final UserDetailsServiceImpl userService;

	public JWTAuthorizationFilter(JWTVerifier jwtVerifier, UserDetailsServiceImpl userService) {
		this.jwtVerifier = jwtVerifier;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		var header = req.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(BEARER_TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		var token = header.replace(BEARER_TOKEN_PREFIX, "");

		try {

			var authentication = authenticate(token);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);

		} catch (JWTVerificationException e) {

			log.debug("Sending 401 Unauthorized error");

			var msg = StringUtils.isNotBlank(e.getLocalizedMessage())
					? e.getLocalizedMessage()
					: HttpStatus.UNAUTHORIZED.getReasonPhrase();

			res.sendError(HttpStatus.UNAUTHORIZED.value(), msg);
		}
	}

	/**
	 * This method is called if the user supplied a jwt token.
	 *
	 * @param token JSON Web Token
	 * @return
	 */
	private UserAccountAuthentication authenticate(String token) {
		DecodedJWT jwt = jwtVerifier.verify(token);

		var userName = jwt.getSubject();
		var userAccount = userService.findByUsername(userName);

		if (userAccount.isPresent() && jwtVerifier.isTokenWhitelisted(token)) {

			var authority = new SimpleGrantedAuthority(jwt.getClaim(JWT_CLAIM_USER_ROLE).asString());

			return new UserAccountAuthentication(userAccount.get(), true, List.of(authority));
		}
		return null;
	}
}
