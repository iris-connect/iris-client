package iris.client_bff.users.web;

import iris.client_bff.users.UserService;
import lombok.AllArgsConstructor;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user-profile")
class UserProfileController {

	private final UserService userService;
	private final UserMapper userMapper;

	@GetMapping
	public UserDtos.Output getUserProfile(Principal principal) {

		var user = userService.findByUsername(principal.getName());

		return user.map(userMapper::toDto).orElse(null);
	}
}
