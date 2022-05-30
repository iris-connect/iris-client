package iris.client_bff.auth.db;

import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Service
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
public class DbAuthUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) {

		UserAccount userAccount = userService.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		// By convention we expect that there exists only one authority and it represents the role
		var role = userAccount.getRole().name();
		var authorities = List.of(new SimpleGrantedAuthority(role));

		return new User(userAccount.getUserName(), userAccount.getPassword(), userAccount.isEnabled(), true, true,
				!userAccount.isLocked(), authorities);
	}
}
