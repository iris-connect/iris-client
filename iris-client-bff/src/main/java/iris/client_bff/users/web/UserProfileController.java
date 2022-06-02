package iris.client_bff.users.web;

import iris.client_bff.users.UserAccount;
import lombok.AllArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user-profile")
class UserProfileController {

	private final UserMapper userMapper;

	@GetMapping
	public UserDtos.Output getUserProfile(@AuthenticationPrincipal UserAccount principal) {
		return userMapper.toDto(principal);
	}
}
