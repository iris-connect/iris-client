package iris.client_bff.users.web;

import static iris.client_bff.users.web.UserMappers.*;
import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.ui.messages.ErrorMessages;
import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.web.dto.UserDTO;
import iris.client_bff.users.web.dto.UserInsertDTO;
import iris.client_bff.users.web.dto.UserListDTO;
import iris.client_bff.users.web.dto.UserRoleDTO;
import iris.client_bff.users.web.dto.UserUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

@Slf4j
@RestController()
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

	private static final String FIELD_ROLE = "role";
	private static final String FIELD_PASSWORD = "password";
	private static final String FIELD_USER_NAME = "userName";
	private static final String FIELD_LAST_NAME = "lastName";
	private static final String FIELD_FIRST_NAME = "firstName";
	private final UserDetailsServiceImpl userService;
	private final ValidationHelper validationHelper;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserListDTO getAllUsers() {
		return new UserListDTO()
				.users(this.userService.loadAll().stream().map(UserMappers::map).collect(Collectors.toList()));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserDTO createUser(@RequestBody @Valid UserInsertDTO userInsert) {

		var userInsertValidated = validateUserInsertDTO(userInsert);

		checkUniqueUsername(userInsertValidated.getUserName());

		return map(userService.create(userInsertValidated));
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public UserDTO updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDTO userUpdateDTO,
			UserAccountAuthentication authentication) {

		var userUpdateDTOValidated = validateUserUpdateDTO(userUpdateDTO);
		var userName = userUpdateDTOValidated.getUserName();

		checkUniqueUsername(userName, id);
		checkOldPassword(userUpdateDTOValidated.getOldPassword(), userUpdateDTOValidated.getPassword(), authentication, id);

		return map(userService.update(id, userUpdateDTOValidated, authentication));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable UUID id, Principal principal) {
		this.userService.deleteById(id, principal.getName());
	}

	private UserUpdateDTO validateUserUpdateDTO(UserUpdateDTO userUpdateDTO) {

		if (userUpdateDTO == null
				|| isToLong(userUpdateDTO.getUserName(), 50)
				|| isToLong(userUpdateDTO.getPassword(), 200)
				|| isToLong(userUpdateDTO.getOldPassword(), 200)
				|| isToLong(userUpdateDTO.getFirstName(), 200)
				|| isToLong(userUpdateDTO.getLastName(), 200)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (validationHelper.isPossibleAttack(userUpdateDTO.getFirstName(), FIELD_FIRST_NAME, true)) {
			userUpdateDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validationHelper.isPossibleAttack(userUpdateDTO.getLastName(), FIELD_LAST_NAME, true)) {
			userUpdateDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		var isInvalid = false;

		var userName = userUpdateDTO.getUserName();
		var password = userUpdateDTO.getPassword();

		if ((isNoneBlank(userName) && validationHelper.isPossibleAttack(userName, FIELD_USER_NAME, false))
				|| (isNotBlank(password) && validationHelper.isPossibleAttackForPassword(password, FIELD_PASSWORD))) {
			isInvalid = true;
		}

		var role = userUpdateDTO.getRole();

		if (role != null && !(role == UserRoleDTO.ADMIN || role == UserRoleDTO.USER)) {
			log.warn(ErrorMessages.INVALID_INPUT + FIELD_ROLE + role);
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (isNoneBlank(password) && !validationHelper.isPasswordValid(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.PW_ERROR_MESSAGE);
		}

		return userUpdateDTO;
	}

	private UserInsertDTO validateUserInsertDTO(UserInsertDTO userInsertDTO) {
		if (userInsertDTO == null
				|| isToLong(userInsertDTO.getUserName(), 50)
				|| isToLong(userInsertDTO.getPassword(), 200)
				|| isToLong(userInsertDTO.getFirstName(), 200)
				|| isToLong(userInsertDTO.getLastName(), 200)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (validationHelper.isPossibleAttack(userInsertDTO.getFirstName(), FIELD_FIRST_NAME, true)) {
			userInsertDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validationHelper.isPossibleAttack(userInsertDTO.getLastName(), FIELD_LAST_NAME, true)) {
			userInsertDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		var isInvalid = false;

		if (validationHelper.isPossibleAttackForRequiredValue(userInsertDTO.getUserName(), FIELD_USER_NAME, false)
				|| validationHelper.isPossibleAttackForPassword(userInsertDTO.getPassword(), FIELD_PASSWORD)) {
			isInvalid = true;
		}

		if (!(userInsertDTO.getRole() == UserRoleDTO.ADMIN || userInsertDTO.getRole() == UserRoleDTO.USER)) {
			log.warn(ErrorMessages.INVALID_INPUT + FIELD_ROLE + userInsertDTO.getRole());
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (!validationHelper.isPasswordValid(userInsertDTO.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.PW_ERROR_MESSAGE);
		}

		return userInsertDTO;
	}

	private boolean isToLong(String value, int maxLength) {
		return StringUtils.length(value) > maxLength;
	}

	private void checkUniqueUsername(String username) {

		userService.findByUsername(username)
				.ifPresent(__ -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserController.username.notunique");
				});
	}

	private void checkUniqueUsername(String username, UUID id) {

		if (isBlank(username)) {
			return;
		}

		userService.findByUsername(username)
				.filter(it -> !it.getUser_id().equals(id))
				.ifPresent(__ -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserController.username.notunique");
				});
	}

	private void checkOldPassword(String oldPassword, String password, UserAccountAuthentication authentication,
			UUID id) {

		if (isBlank(password)
				|| (authentication.isAdmin() && !userService.isItCurrentUser(id, authentication))) {
			return;
		}

		if (!userService.isOldPasswordCorrect(id, oldPassword)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserController.oldpassword.wrong");
		}
	}
}
