package iris.client_bff.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Getter
@Setter
@ConfigurationProperties("tokens")
@Validated
public class TokenConfig {

	@Value("${tokens.datLength: #{40}}")
	private int datLength = 40;

	@Value("${tokens.catLength: #{40}}")
	private int catLength = 40;

	/**
	 * Reason for the default here in the code:
	 * <p>
	 * Determining the tokens with PBKDF2 (10000 iterations) for our possible 1e21 combinations of the initial token is a
	 * lot of effort for the limited benefit of possible data deliveries. On the other hand, since the salt must be the
	 * same for correct operation in all health departments and apps, we use a fixed default salt. However, this can be
	 * changed via configuration if required.
	 * </p>
	 */
	@Value("${tokens.datSalt: 1ILvLVngzousCAm6r2YmB7qDwX7w5kwD}")
	private String datSalt;

	/**
	 * Reason for the default here in the code: lock at {@link TokenConfig#datSalt}
	 */
	@Value("${tokens.catSalt: VtXKYoNmvme86V2zrjdWg9NglGQfAAUB}")
	private String catSalt;

	@NotBlank(message = "Token generator salt must be set. (Environment variable: TOKENS_GENERATOR_SALT)")
	@Length(min = 16, max = 32,
			message = "Token generator salt must be between 16 and 32 Chars. (Environment variable: TOKENS_GENERATOR_SALT)")
	private String generatorSalt;

}
