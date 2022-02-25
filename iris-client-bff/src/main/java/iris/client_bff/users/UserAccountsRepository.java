package iris.client_bff.users;

import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import iris.client_bff.users.entities.UserRole;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountsRepository extends JpaRepository<UserAccount, UserAccountIdentifier> {
	Optional<UserAccount> findByUserName(String userName);

	long countByRole(UserRole role);
}
