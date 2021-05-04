package iris.client_bff.auth.db.jwt;

import com.auth0.jwt.JWTCreator.Builder;

public interface JWTSigner {

	String sign(Builder builder);

}
