package de.healthIMIS.iris.client.auth.db.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface JWTVerifier {

  DecodedJWT verify(String jwt);

}
