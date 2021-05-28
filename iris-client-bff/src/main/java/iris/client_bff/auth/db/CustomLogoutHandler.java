package iris.client_bff.auth.db;

import com.auth0.jwt.interfaces.DecodedJWT;
import iris.client_bff.auth.db.jwt.JWTService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static iris.client_bff.auth.db.SecurityConstants.BEARER_TOKEN_PREFIX;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    private final JWTService jwtService;

    public CustomLogoutHandler(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    // Annotation necessary because of multiple DB calls
    // see https://stackoverflow.com/a/32552558
    @Transactional
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER_TOKEN_PREFIX)) {
            return;
        }
        var token = header.replace(BEARER_TOKEN_PREFIX, "");
        DecodedJWT jwt = jwtService.verify(token);
        jwtService.invalidateTokensOfUser(jwt.getSubject());
    }
}
