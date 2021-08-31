package iris.client_bff.auth.db;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "security.auth.db")
@ConstructorBinding
@Data
@AllArgsConstructor
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@Validated
public class DbAuthProperties {

	@NotEmpty(
			message = "The admin user name must be configured! (Environment variable: SECURITY_AUTH_DB_ADMIN_USER_NAME)")
	private String adminUserName;

	@NotEmpty(
			message = "The admin user password must be configured! (Environment variable: SECURITY_AUTH_DB_ADMIN_USER_PASSWORD)")
	private String adminUserPassword;
}
