package iris.client_bff.users;

import iris.client_bff.users.UserAccount.UserAccountIdentifier;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

interface UserAccountsRepository extends JpaRepository<UserAccount, UserAccountIdentifier> {

	String SELECT_BASE = "SELECT u from UserAccount u where u.role != iris.client_bff.users.UserRole.DELETED";
	String DELETED_NULL = " AND u.deletedAt IS NULL";
	String NOT_ANONYMOUS = " AND u.role != iris.client_bff.users.UserRole.ANONYMOUS";

	@Query(SELECT_BASE + DELETED_NULL + " AND u.userName = :userName")
	Optional<UserAccount> findUserByUsername(String userName);

	@Query(SELECT_BASE + DELETED_NULL + NOT_ANONYMOUS)
	List<UserAccount> findAllUsers();

	@Query(SELECT_BASE + " AND u.deletedAt IS NOT NULL")
	Streamable<UserAccount> findAllDeleted();

	@Query(SELECT_BASE + DELETED_NULL + " AND u.id = :id")
	Optional<UserAccount> findUserById(UserAccountIdentifier id);

	@Query("SELECT count(u) from UserAccount u where u.role = :role" + DELETED_NULL)
	long countUsersByRole(UserRole role);
}
