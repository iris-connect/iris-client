package de.healthIMIS.iris.client.users;

import de.healthIMIS.iris.client.users.entities.UserAccount;
import de.healthIMIS.iris.client.users.entities.UserRole;
import de.healthIMIS.iris.client.users.web.dto.UserInsertDTO;
import de.healthIMIS.iris.client.users.web.dto.UserUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserAccountsRepository userAccountsRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserAccount userAccount = userAccountsRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		// By convention we expect that there exists only one authority and it represents the role
		var role = userAccount.getRole().name();
		var authorities = List.of(new SimpleGrantedAuthority(role));

		return new User(userAccount.getUserName(), userAccount.getPassword(), authorities);
	}

	public Optional<UserAccount> findByUsername(String username) {
		return userAccountsRepository.findByUserName(username);
	}

	public List<UserAccount> loadAll() {
		return StreamSupport
				.stream(userAccountsRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
	}

	public UserAccount create(UserInsertDTO userInsertDTO) {
		log.info("Create user {}", userInsertDTO.getUserName());
		var userAccount = map(userInsertDTO);
		return userAccountsRepository.save(userAccount);
	}

	public UserAccount update(UUID userId, UserUpdateDTO userUpdateDTO) {
		log.info("Update user: {}", userId);

		var optional = userAccountsRepository.findById(userId);
		if (optional.isEmpty()) {
			var error = "User not found: " + userId.toString();
			log.error(error);
			throw new RuntimeException(error);
		}

		var userAccount = optional.get();
		userAccount.setLastName(userUpdateDTO.getLastName());
		userAccount.setFirstName(userUpdateDTO.getFirstName());

		var newUserName = userUpdateDTO.getUserName();
		if (StringUtils.isNotEmpty(newUserName)) {
			userAccount.setUserName(newUserName);
		}

		var newRole = userUpdateDTO.getRole();
		if (newRole != null) {
			userAccount.setRole(UserRole.valueOf(newRole.name()));
		}

		var newPassword = userUpdateDTO.getPassword();
		if (StringUtils.isNotEmpty(newPassword)) {
			userAccount.setPassword(passwordEncoder.encode(newPassword));
		}

		return userAccountsRepository.save(userAccount);
	}

	public void deleteById(UUID id) {
		log.info("Delete user: {}", id);
		userAccountsRepository.deleteById(id);
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
}
