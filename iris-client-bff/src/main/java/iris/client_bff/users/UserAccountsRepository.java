package iris.client_bff.users;

import iris.client_bff.users.entities.UserAccount;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface UserAccountsRepository extends CrudRepository<UserAccount, UUID> {
	Optional<UserAccount> findByUserName(String userName);
}
