package iris.client_bff.auth.db;

import static iris.client_bff.users.UserRole.*;

import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
@Slf4j
class InitialAdminLoader {

	private final InitialAdminLoader.Properties conf;

	private final UserService userService;

	private final PasswordEncoder passwordEncoder;

	@PostConstruct
	protected void createAdminUserIfNotExists() {

		var userName = conf.getAdminUserName();

		if (userService.findByUsername(userName).isEmpty()) {

			log.info("Admin user [{}] does not exist yet.", userName);

			var userAccount = new UserAccount();
			userAccount.setUserName(userName);
			userAccount.setPassword(passwordEncoder.encode(conf.getAdminUserPassword()));
			userAccount.setFirstName("admin");
			userAccount.setLastName("admin");
			userAccount.setRole(ADMIN);

			userService.create(userAccount);

		} else {
			log.info("Admin user [{}] already exists. Skip creating admin user.", userName);
		}
	}

	@ConfigurationProperties(prefix = "security.auth.db")
	@ConstructorBinding
	@ConditionalOnProperty(
			value = "security.auth",
			havingValue = "db")
	@Validated
	@Value
	static class Properties {

		@NotEmpty(
				message = "The admin user name must be configured! (Environment variable: SECURITY_AUTH_DB_ADMIN_USER_NAME)")
		private String adminUserName;

		@NotEmpty(
				message = "The admin user password must be configured! (Environment variable: SECURITY_AUTH_DB_ADMIN_USER_PASSWORD)")
		private String adminUserPassword;
	}
}
