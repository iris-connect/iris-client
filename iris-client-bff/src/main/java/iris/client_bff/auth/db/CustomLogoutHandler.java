package iris.client_bff.auth.db;

import static iris.client_bff.auth.db.SecurityConstants.*;

import iris.client_bff.auth.db.jwt.JWTService;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;

@Service
@AllArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

	private final JWTService jwtService;

	@Override
	public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) {
		String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.isEmpty(header) || !header.startsWith(BEARER_TOKEN_PREFIX)) {
			return;
		}
		var token = header.replace(BEARER_TOKEN_PREFIX, "");
		DecodedJWT jwt = jwtService.verify(token);
		jwtService.invalidateTokensOfUser(jwt.getSubject());
	}
}
