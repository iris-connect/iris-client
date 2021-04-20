package de.healthIMIS.iris.client.auth.db;

import com.auth0.jwt.interfaces.DecodedJWT;
import de.healthIMIS.iris.client.auth.db.jwt.JWTVerifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static de.healthIMIS.iris.client.auth.db.SecurityConstants.BEARER_TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


    public JWTAuthorizationFilter(AuthenticationManager authManager, JWTVerifier jwtVerifier) {
        super(authManager);
        this.jwtVerifier = jwtVerifier;
    }

    public JWTVerifier jwtVerifier;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER_TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        var token = header.replace(BEARER_TOKEN_PREFIX, "");

        var authentication = authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UserAccountAuthentication authenticate(String token) {
        DecodedJWT jwt = jwtVerifier.verify(token);

        String user = jwt.getSubject();

        GrantedAuthority role = new SimpleGrantedAuthority("USER");

        if (user != null) {
            return new UserAccountAuthentication(user, true, Collections.singletonList(role));
        }
        return null;
    }

}
