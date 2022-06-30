package iris.client_bff.users;

import iris.client_bff.users.UserAccount.UserAccountIdentifier;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jens Kutzsche
 */
public interface UserAccountsRepositoryForTests extends JpaRepository<UserAccount, UserAccountIdentifier> {

	UserAccount findByUserName(String userName);
}
