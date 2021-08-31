package iris.client_bff.users.web;

import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.web.dto.UserDTO;
import lombok.AllArgsConstructor;

import java.security.Principal;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user-profile")
public class UserProfileController {

	private final UserDetailsServiceImpl userService;

	@GetMapping
	public UserDTO getUserProfile(Principal principal) {
		Optional<UserAccount> byUsername = userService.findByUsername(principal.getName());
		if (byUsername.isEmpty()) {
			return null;
		} else {
			return UserMappers.map(byUsername.get());
		}
	}
}
