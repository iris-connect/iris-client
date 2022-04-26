package iris.client_bff.users;

import static java.util.Objects.*;
import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import iris.client_bff.users.entities.UserRole;
import iris.client_bff.users.web.dto.UserRoleDTO;
import iris.client_bff.users.web.dto.UserUpdateDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserAccountsRepository userAccountsRepository;
	private final PasswordEncoder passwordEncoder;
	private final JWTService jwtService;

	@Override
	public UserDetails loadUserByUsername(String username) {

		UserAccount userAccount = userAccountsRepository.findByUserNameAndDeletedAtIsNull(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		// By convention we expect that there exists only one authority and it represents the role
		var role = userAccount.getRole().name();
		var authorities = List.of(new SimpleGrantedAuthority(role));

		return new User(userAccount.getUserName(), userAccount.getPassword(), userAccount.isEnabled(), true, true,
				!userAccount.isLocked(), authorities);
	}

	public Optional<UserAccount> findByUuid(UUID id) {
		return userAccountsRepository.findById(UserAccountIdentifier.of(id));
	}

	public Optional<UserAccount> findByUsername(String username) {
		return userAccountsRepository.findByUserNameAndDeletedAtIsNull(username);
	}

	public boolean isOldPasswordCorrect(@NonNull UserAccountIdentifier id, @NonNull String oldPassword) {

		return userAccountsRepository.findByIdAndDeletedAtIsNull(id)
				.map(UserAccount::getPassword)
				.filter(it -> passwordEncoder.matches(oldPassword, it))
				.isPresent();
	}

	public List<UserAccount> loadAll() {
		return userAccountsRepository.findAllByDeletedAtIsNull();
	}

	public UserAccount create(UserAccount user) {
		log.info("Create user {}", user.getUserName());
		return userAccountsRepository.save(user);
	}

	public UserAccount update(UserAccountIdentifier userId, UserUpdateDTO userUpdateDTO,
			UserAccountAuthentication authentication) {

		log.info("Update user: {}", userId);

		var userAccount = loadUser(userId);

		var isAdmin = authentication.isAdmin();
		var oldUserName = userAccount.getUserName();
		var invalidateTokens = false;

		if (!isAdmin && !isItCurrentUser(userId, authentication)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change other users!");
		}

		var lastName = userUpdateDTO.lastName();
		if (nonNull(lastName)) {
			userAccount.setLastName(lastName);
		}

		var firstName = userUpdateDTO.firstName();
		if (nonNull(firstName)) {
			userAccount.setFirstName(firstName);
		}

		var newUserName = userUpdateDTO.userName();
		if (!isAdmin && isNotBlank(newUserName)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change his username!");
		}

		if (isNotBlank(newUserName)
				&& !StringUtils.equals(oldUserName, newUserName)) {

			userAccount.setUserName(newUserName);
			invalidateTokens = true;
		}

		var newRoleDto = userUpdateDTO.role();
		if (!isAdmin && newRoleDto != null) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change his role!");
		}

		if (newRoleDto != null) {

			if (isRemoveLastAdmin(userAccount, newRoleDto)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role of the last admin can't be changed!");
			}

			var newRole = UserRole.valueOf(newRoleDto.name());

			if (userAccount.getRole() != newRole) {
				userAccount.setRole(newRole);
				invalidateTokens = true;
			}
		}

		var newPassword = userUpdateDTO.password();
		if (isNotBlank(newPassword) && !passwordEncoder.matches(newPassword, userAccount.getPassword())) {

			userAccount.setPassword(passwordEncoder.encode(newPassword));
			invalidateTokens = true;
		}

		var locked = userUpdateDTO.locked();
		if (locked != null) {
			var changed = updateLockState(authentication, userAccount, locked);
			invalidateTokens = changed || invalidateTokens;
		}

		if (invalidateTokens) {
			jwtService.invalidateTokensOfUser(oldUserName);
		}

		return userAccountsRepository.save(userAccount);
	}

	private boolean updateLockState(UserAccountAuthentication authentication, UserAccount userAccount, boolean locked) {

		if (userAccount.isLocked() == locked) {
			return false;
		}

		if (!authentication.isAdmin()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Only an admin user can change the lock state of an user!");
		}

		if (isItCurrentUser(userAccount.getId(), authentication)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user may not change his own lock state!");
		}

		userAccount.setLocked(locked);
		return true;
	}

	public void deleteById(UserAccountIdentifier id, String currentUserName) {

		userAccountsRepository.findByIdAndDeletedAtIsNull(id)
				.ifPresent(account -> {

					if (account.getUserName().equals(currentUserName)) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user may not delete himself!");
					}

					log.info("Delete User: {}", id);

					jwtService.invalidateTokensOfUser(account.getUserName());

					userAccountsRepository.save(account.markDeleted());
				});
	}

	public boolean isItCurrentUser(UserAccountIdentifier userId, UserAccountAuthentication authentication) {

		var userAccount = loadUser(userId);

		return authentication.getName().equals(userAccount.getUserName());
	}

	private UserAccount loadUser(UserAccountIdentifier userId) {

		return userAccountsRepository.findByIdAndDeletedAtIsNull(userId)
				.orElseThrow(() -> {
					var error = "User not found: " + userId.toString();
					log.error(error);
					throw new RuntimeException(error);
				});
	}

	private boolean isRemoveLastAdmin(UserAccount userAccount, UserRoleDTO newRoleDto) {
		return newRoleDto != UserRoleDTO.ADMIN
				&& userAccount.getRole() == UserRole.ADMIN
				&& userAccountsRepository.countByRoleAndDeletedAtIsNull(UserRole.ADMIN) == 1;
	}
}
