package iris.client_bff.users.web;

import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

	private static final String AS_ADMIN = "hasAuthority('ADMIN')";

	private final UserService userService;
	private final UserMapper userMapper;

	@GetMapping
	@PreAuthorize(AS_ADMIN)
	public UserDtos.OutputList getAllUsers() {
		return new UserDtos.OutputList(this.userService.loadAll().stream().map(userMapper::toDto).toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize(AS_ADMIN)
	public UserDtos.Output createUser(@RequestBody @Valid UserDtos.Insert userInsert) {

		checkUniqueUsername(userInsert.userName());

		return userMapper.toDto(userService.create(userMapper.fromDto(userInsert)));
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public UserDtos.Output updateUser(@PathVariable UserAccountIdentifier id,
			@RequestBody @Valid UserDtos.Update userUpdateDTO,
			@AuthenticationPrincipal UserAccount principal) {

		checkUniqueUsername(userUpdateDTO.userName(), id);
		checkOldPassword(principal, id, userUpdateDTO.oldPassword(), userUpdateDTO.password());

		return userMapper.toDto(userService.update(id,
				userUpdateDTO.lastName(),
				userUpdateDTO.firstName(),
				userUpdateDTO.userName(),
				userUpdateDTO.password(),
				userMapper.fromDto(userUpdateDTO.role()),
				userUpdateDTO.locked()));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize(AS_ADMIN)
	public void deleteUser(@PathVariable UserAccountIdentifier id) {
		this.userService.deleteById(id);
	}

	private void checkUniqueUsername(String username) {

		userService.findByUsername(username)
				.ifPresent(__ -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserController.username.notunique");
				});
	}

	private void checkUniqueUsername(String username, UserAccountIdentifier id) {

		if (isBlank(username)) {
			return;
		}

		userService.findByUsername(username)
				.filter(it -> !it.getId().equals(id))
				.ifPresent(__ -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserController.username.notunique");
				});
	}

	private void checkOldPassword(UserAccount principal, UserAccountIdentifier id, String oldPassword, String password) {

		if (isBlank(password) || (principal.isAdmin() && !userService.isItCurrentUser(id))) {
			return;
		}

		if (!userService.isOldPasswordCorrect(id, oldPassword)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserController.oldpassword.wrong");
		}
	}
}
