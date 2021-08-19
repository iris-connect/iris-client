package iris.client_bff.auth.db;

import iris.client_bff.users.UserAccountsRepository;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
public class InitialAdminLoader {

	private DbAuthProperties conf;

	private UserAccountsRepository repo;

	private PasswordEncoder passwordEncoder;

	@PostConstruct
	protected void createAdminUserIfNotExists() {

		var userName = conf.getAdminUserName();

		if (repo.findByUserName(userName).isEmpty()) {

			log.info("Create admin user [{}]", userName);

			var userAccount = new UserAccount();
			userAccount.setUserName(userName);
			userAccount.setPassword(passwordEncoder.encode(conf.getAdminUserPassword()));
			userAccount.setFirstName("admin");
			userAccount.setLastName("admin");
			userAccount.setRole(UserRole.ADMIN);

			repo.save(userAccount);

		} else {
			log.info("Admin user [{}] already exists. Skip creating admin user.", userName);
		}
	}
}
