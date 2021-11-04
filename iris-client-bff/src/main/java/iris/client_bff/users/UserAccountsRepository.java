package iris.client_bff.users;

import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountsRepository extends JpaRepository<UserAccount, UUID> {
	Optional<UserAccount> findByUserName(String userName);

	long countByRole(UserRole role);
}
