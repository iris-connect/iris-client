package iris.client_bff.auth.db.jwt;

import lombok.Value;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "security.jwt")
@ConstructorBinding
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@Validated
@Value
public class JwtProperties {

	private @NotBlank String sharedSecret;
}
