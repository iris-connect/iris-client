package iris.client_bff.users;

import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import iris.client_bff.users.entities.UserRole;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

interface UserAccountsRepository extends JpaRepository<UserAccount, UserAccountIdentifier> {

	String SELECT_BASE = "SELECT u from UserAccount u where u.role != iris.client_bff.users.entities.UserRole.DELETED AND ";

	@Query(SELECT_BASE + "u.userName = :userName AND u.deletedAt IS NULL")
	Optional<UserAccount> findUserByUsername(String userName);

	@Query(SELECT_BASE + "u.deletedAt IS NULL")
	List<UserAccount> findAllUsers();

	@Query(SELECT_BASE + "u.deletedAt IS NOT NULL")
	Streamable<UserAccount> findAllDeleted();

	@Query(SELECT_BASE + "u.id = :id AND u.deletedAt IS NULL")
	Optional<UserAccount> findUserById(UserAccountIdentifier id);

	@Query("SELECT count(u) from UserAccount u where u.role = :role AND u.deletedAt IS NULL")
	long countUsersByRole(UserRole role);
}
