package iris.client_bff.config;

import lombok.Value;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("tokens")
@ConstructorBinding
@Value
@Validated
public class TokenProperties {

	@NotBlank(message = "Token generator salt must be set. (Environment variable: TOKENS_GENERATOR_SALT)") //
	@Length(min = 16, max = 32,
			message = "Token generator salt must be between 16 and 32 Chars. (Environment variable: TOKENS_GENERATOR_SALT)")
	private String generatorSalt;
}
