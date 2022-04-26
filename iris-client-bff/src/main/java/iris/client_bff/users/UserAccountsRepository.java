package iris.client_bff.users;

import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import iris.client_bff.users.entities.UserRole;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

interface UserAccountsRepository extends JpaRepository<UserAccount, UserAccountIdentifier> {

	Optional<UserAccount> findByUserNameAndDeletedAtIsNull(String userName);

	List<UserAccount> findAllByDeletedAtIsNull();

	Streamable<UserAccount> findAllByDeletedAtIsNotNull();

	Optional<UserAccount> findByIdAndDeletedAtIsNull(UserAccountIdentifier id);

	long countByRoleAndDeletedAtIsNull(UserRole role);
}
