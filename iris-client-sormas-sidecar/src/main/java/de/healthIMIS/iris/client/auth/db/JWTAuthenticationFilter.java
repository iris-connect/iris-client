package de.healthIMIS.iris.client.auth.db;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.healthIMIS.iris.client.auth.db.dto.LoginRequestDTO;
import de.healthIMIS.iris.client.auth.db.jwt.JWTSigner;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static de.healthIMIS.iris.client.auth.db.SecurityConstants.*;


@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  private JWTSigner jwtSigner;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {
    try {

      LoginRequestDTO loginRequest = new ObjectMapper()
          .readValue(req.getInputStream(), LoginRequestDTO.class);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUserName(),
              loginRequest.getPassword(),
              new ArrayList<>())
      );

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain,
      Authentication auth) {

    User user = (User) auth.getPrincipal();

    String token = jwtSigner.sign(JWT.create()
        .withSubject(user.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)));

    res.addHeader(AUTHENTICATION_INFO, BEARER_TOKEN_PREFIX + token);
    res.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, AUTHENTICATION_INFO);

  }


}
