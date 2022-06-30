package iris.client_bff.auth.db;

import static iris.client_bff.auth.db.MfAuthenticationProperties.MfAuthenticationOptions.*;

import lombok.Value;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "security.auth.db.mfa")
@ConstructorBinding
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@Value
public class MfAuthenticationProperties {

	private MfAuthenticationOptions option;

	public boolean isMfaEnabled() {
		return option != DISABLED;
	}

	public enum MfAuthenticationOptions {
		ALWAYS, OPTIONAL_DEFAULT_TRUE, OPTIONAL_DEFAULT_FALSE, DISABLED
	}
}
