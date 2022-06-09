package iris.client_bff.users;

import static iris.client_bff.users.UserRole.*;
import static java.util.Objects.*;
import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.users.UserAccount.UserAccountBuilder;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserAccountsRepository users;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticatedUserAware authManager;

	public Optional<UserAccount> findByUuid(UUID id) {
		return users.findById(UserAccountIdentifier.of(id));
	}

	public Optional<UserAccount> findByUsername(String username) {
		return users.findUserByUsername(username);
	}

	public boolean isOldPasswordCorrect(@NonNull UserAccountIdentifier id, @NonNull String oldPassword) {

		return users.findUserById(id)
				.map(UserAccount::getPassword)
				.filter(it -> passwordEncoder.matches(oldPassword, it))
				.isPresent();
	}

	public List<UserAccount> loadAll() {
		return users.findAllUsers();
	}

	public UserAccount create(UserAccount user) {
		log.info("Create user {}", user.getUserName());
		return users.save(user);
	}

	public UserAccount update(UserAccountIdentifier userId,
			@Nullable String lastName, @Nullable String firstName,
			@Nullable String userName, @Nullable String password, @Nullable UserRole role, @Nullable Boolean locked) {

		log.info("Update user: {}", userId);

		var userAccount = loadUser(userId);

		var isAdmin = authManager.isAdmin();
		var oldUserName = userAccount.getUserName();
		var invalidateTokens = false;

		if (!isAdmin && !isItCurrentUser(userId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change other users!");
		}

		if (nonNull(lastName)) {
			userAccount.setLastName(lastName);
		}

		if (nonNull(firstName)) {
			userAccount.setFirstName(firstName);
		}

		if (!isAdmin && isNotBlank(userName)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change his username!");
		}

		if (isNotBlank(userName)
				&& !StringUtils.equals(oldUserName, userName)) {

			userAccount.setUserName(userName);
			invalidateTokens = true;
		}

		if (role != null) {

			if (!isAdmin) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change his role!");
			}

			if (isRemoveLastAdmin(userAccount, role)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role of the last admin can't be changed!");
			}

			if (userAccount.getRole() != role) {
				userAccount.setRole(role);
				invalidateTokens = true;
			}
		}

		if (isNotBlank(password) && !passwordEncoder.matches(password, userAccount.getPassword())) {

			userAccount.setPassword(passwordEncoder.encode(password));
			invalidateTokens = true;
		}

		if (locked != null) {
			var changed = updateLockState(userAccount, locked);
			invalidateTokens = changed || invalidateTokens;
		}

		if (invalidateTokens) {
			userAccount.markLoginIncompatiblyUpdated();
		}

		return users.save(userAccount);
	}

	private boolean updateLockState(UserAccount userAccount, boolean locked) {

		if (!authManager.isAdmin()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Only an admin user can change the lock state of an user!");
		}

		if (isItCurrentUser(userAccount.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user may not change his own lock state!");
		}

		if (userAccount.isLocked() == locked) {
			return false;
		}

		userAccount.setLocked(locked);
		return true;
	}

	public void deleteById(UserAccountIdentifier id) {

		if (isItCurrentUser(id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user may not delete himself!");
		}

		users.findUserById(id)
				.ifPresent(account -> {

					log.info("Delete User: {}", id);

					users.save(account.markDeleted());
				});
	}

	public boolean isItCurrentUser(UserAccountIdentifier userId) {
		return Objects.equals(userId, getCurrentUser().getId());
	}

	/**
	 * If the user does not exist yet, it will be created.
	 * <p/>
	 * Default values are included:
	 * <ul>
	 * <li>`userName` = value of `{username}`</li>
	 * <li>`password` = random UUID</li>
	 * <li>`role` = `ANONYMOUS`</li>
	 * <li>`locked` = `false`</li>
	 * <li>`deletedAt` = null</li>
	 * </ul>
	 *
	 * @param username
	 * @param defineUserFunction
	 * @return The found or newly created user.
	 */
	public UserAccount findOrCreateUser(String username, UnaryOperator<UserAccountBuilder> defineUserFunction) {

		return findByUsername(username)
				.orElseGet(() -> {

					var builder = UserAccount.builder()
							.userName(username)
							.password(UUID.randomUUID().toString())
							.role(ANONYMOUS);

					var user = defineUserFunction.apply(builder).build();

					return create(user);
				});
	}

	private UserAccount getCurrentUser() {
		return authManager.getCurrentUser().orElseThrow();
	}

	private UserAccount loadUser(UserAccountIdentifier userId) {

		return users.findUserById(userId)
				.orElseThrow(() -> {
					var error = "User not found: " + userId.toString();
					log.error(error);
					throw new RuntimeException(error);
				});
	}

	private boolean isRemoveLastAdmin(UserAccount userAccount, UserRole newRole) {
		return newRole != UserRole.ADMIN
				&& userAccount.getRole() == UserRole.ADMIN
				&& users.countUsersByRole(UserRole.ADMIN) == 1;
	}
}
