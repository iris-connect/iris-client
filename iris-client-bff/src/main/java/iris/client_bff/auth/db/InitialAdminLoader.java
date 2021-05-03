package iris.client_bff.auth.db;

import iris.client_bff.users.UserAccountsRepository;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
        value="security.auth",
        havingValue = "db"
)
public class InitialAdminLoader {

    private DbAuthProperties conf;

    private UserAccountsRepository repo;

    private PasswordEncoder passwordEncoder;

    @PostConstruct
    protected void initializeAdmin() {
        if (conf.getAdminUserName() != null && conf.getAdminUserPassword() != null) {
            createAdminUserIfNotExists();
        } else {
            log.info("No admin user configured. Skip creating admin user.");
        }
    }

    private void createAdminUserIfNotExists() {
        if (repo.findByUserName(conf.getAdminUserName()).isEmpty()) {
            log.info("Create admin user [{}]", conf.getAdminUserName());
            var userAccount = new UserAccount();
            userAccount.setUserName(conf.getAdminUserName());
            userAccount.setPassword(passwordEncoder.encode(conf.getAdminUserPassword()));
            userAccount.setFirstName("admin");
            userAccount.setLastName("admin");
            userAccount.setRole(UserRole.ADMIN);

            repo.save(userAccount);
        } else {
            log.info("Admin user [{}] already exists. Skip creating admin user.", conf.getAdminUserName());
        }

    }

}
