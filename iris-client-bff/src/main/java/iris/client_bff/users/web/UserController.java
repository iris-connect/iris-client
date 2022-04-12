package iris.client_bff.users.web;

import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import iris.client_bff.users.web.dto.UserDTO;
import iris.client_bff.users.web.dto.UserInsertDTO;
import iris.client_bff.users.web.dto.UserListDTO;
import iris.client_bff.users.web.dto.UserUpdateDTO;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

	private final UserDetailsServiceImpl userService;
	private final UserMapper userMapper;

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserListDTO getAllUsers() {
		return new UserListDTO()
				.users(this.userService.loadAll().stream().map(userMapper::toDto).toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserDTO createUser(@RequestBody @Valid UserInsertDTO userInsert) {

		checkUniqueUsername(userInsert.getUserName());

		return userMapper.toDto(userService.create(userInsert));
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public UserDTO updateUser(@PathVariable UserAccountIdentifier id, @RequestBody @Valid UserUpdateDTO userUpdateDTO,
			UserAccountAuthentication authentication) {

		checkUniqueUsername(userUpdateDTO.getUserName(), id);
		checkOldPassword(userUpdateDTO.getOldPassword(), userUpdateDTO.getPassword(), authentication, id);

		return userMapper.toDto(userService.update(id, userUpdateDTO, authentication));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable UserAccountIdentifier id, Principal principal) {

		this.userService.deleteById(id, principal.getName());
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

	private void checkOldPassword(String oldPassword, String password, UserAccountAuthentication authentication,
			UserAccountIdentifier id) {

		if (isBlank(password)
				|| (authentication.isAdmin() && !userService.isItCurrentUser(id, authentication))) {
			return;
		}

		if (!userService.isOldPasswordCorrect(id, oldPassword)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserController.oldpassword.wrong");
		}
	}
}
