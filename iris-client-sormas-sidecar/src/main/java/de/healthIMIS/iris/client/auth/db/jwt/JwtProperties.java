package de.healthIMIS.iris.client.auth.db.jwt;

import lombok.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.jwt")
@ConditionalOnProperty(
        value="security.auth",
        havingValue = "db"
)
public class JwtProperties {

  private @NonNull String jwtSharedSecret;

}
