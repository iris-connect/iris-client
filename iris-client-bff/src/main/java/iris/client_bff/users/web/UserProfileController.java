package iris.client_bff.users.web;

import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.web.dto.UserDTO;
import lombok.AllArgsConstructor;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user-profile")
class UserProfileController {

	private final UserDetailsServiceImpl userService;
	private final UserMapper userMapper;

	@GetMapping
	public UserDTO getUserProfile(Principal principal) {

		var user = userService.findByUsername(principal.getName());

		return user.map(userMapper::toDto).orElse(null);
	}
}
