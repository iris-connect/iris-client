package iris.client_bff.users;

import static java.util.Objects.*;
import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import iris.client_bff.users.entities.UserRole;
import iris.client_bff.users.web.dto.UserInsertDTO;
import iris.client_bff.users.web.dto.UserRoleDTO;
import iris.client_bff.users.web.dto.UserUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
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
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserAccountsRepository userAccountsRepository;

	private final PasswordEncoder passwordEncoder;

	private final JWTService jwtService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserAccount userAccount = userAccountsRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		// By convention we expect that there exists only one authority and it represents the role
		var role = userAccount.getRole().name();
		var authorities = List.of(new SimpleGrantedAuthority(role));

		return new User(userAccount.getUserName(), userAccount.getPassword(), authorities);
	}

	public Optional<UserAccount> findByUuid(UUID id) {
		return userAccountsRepository.findById(UserAccountIdentifier.of(id));
	}

	public Optional<UserAccount> findByUsername(String username) {
		return userAccountsRepository.findByUserName(username);
	}

	public boolean isOldPasswordCorrect(@NonNull UserAccountIdentifier id, @NonNull String oldPassword) {

		return userAccountsRepository.findById(id)
				.map(UserAccount::getPassword)
				.filter(it -> passwordEncoder.matches(oldPassword, it))
				.isPresent();
	}

	public List<UserAccount> loadAll() {
		return userAccountsRepository.findAll();
	}

	public UserAccount create(UserInsertDTO userInsertDTO) {
		log.info("Create user {}", userInsertDTO.getUserName());
		var userAccount = map(userInsertDTO);
		return userAccountsRepository.save(userAccount);
	}

	public UserAccount update(UserAccountIdentifier userId, UserUpdateDTO userUpdateDTO,
			UserAccountAuthentication authentication) {

		log.info("Update user: {}", userId);

		var userAccount = loadUser(userId);

		var isAdmin = authentication.isAdmin();
		var invalidateTokens = false;

		var oldUserName = userAccount.getUserName();
		if (!isAdmin && !oldUserName.equals(authentication.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change other users!");
		}

		var lastName = userUpdateDTO.getLastName();
		if (nonNull(lastName)) {
			userAccount.setLastName(lastName);
		}

		var firstName = userUpdateDTO.getFirstName();
		if (nonNull(firstName)) {
			userAccount.setFirstName(firstName);
		}

		var newUserName = userUpdateDTO.getUserName();
		if (!isAdmin && isNotBlank(newUserName)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change his username!");

		} else if (isNotBlank(newUserName)
				&& !StringUtils.equals(oldUserName, newUserName)) {

			userAccount.setUserName(newUserName);
			invalidateTokens = true;
		}

		var newRoleDto = userUpdateDTO.getRole();
		if (!isAdmin && newRoleDto != null) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change his role!");

		} else if (isRemoveLastAdmin(userAccount, newRoleDto)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role of the last admin can't be changed!");

		} else if (newRoleDto != null) {

			var newRole = UserRole.valueOf(newRoleDto.name());

			if (userAccount.getRole() != newRole) {
				userAccount.setRole(newRole);
				invalidateTokens = true;
			}
		}

		var newPassword = userUpdateDTO.getPassword();
		if (isNotBlank(newPassword) && !passwordEncoder.matches(newPassword, userAccount.getPassword())) {

			userAccount.setPassword(passwordEncoder.encode(newPassword));
			invalidateTokens = true;
		}

		if (invalidateTokens) {
			jwtService.invalidateTokensOfUser(oldUserName);
		}

		return userAccountsRepository.save(userAccount);
	}

	public void deleteById(UserAccountIdentifier id, String currentUserName) {

		userAccountsRepository.findById(id)
				.ifPresent(it -> {

					if (it.getUserName().equals(currentUserName)) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user may not delete himself!");
					}

					log.info("Delete User: {}", id);

					jwtService.invalidateTokensOfUser(it.getUserName());
				});

		userAccountsRepository.deleteById(id);
	}

	public boolean isItCurrentUser(UserAccountIdentifier userId, UserAccountAuthentication authentication) {

		var userAccount = loadUser(userId);

		return authentication.getName().equals(userAccount.getUserName());
	}

	private UserAccount loadUser(UserAccountIdentifier userId) {

		return userAccountsRepository.findById(userId)
				.orElseThrow(() -> {
					var error = "User not found: " + userId.toString();
					log.error(error);
					throw new RuntimeException(error);
				});
	}

	private UserAccount map(UserInsertDTO userInsertDTO) {
		var user = new UserAccount();
		user.setRole(UserRole.valueOf(userInsertDTO.getRole().name()));
		user.setUserName(userInsertDTO.getUserName());
		user.setLastName(userInsertDTO.getLastName());
		user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
		user.setFirstName(userInsertDTO.getFirstName());
		return user;
	}

	private boolean isRemoveLastAdmin(UserAccount userAccount, UserRoleDTO newRoleDto) {
		return newRoleDto != UserRoleDTO.ADMIN
				&& userAccount.getRole() == UserRole.ADMIN
				&& userAccountsRepository.countByRole(UserRole.ADMIN) == 1;
	}
}
