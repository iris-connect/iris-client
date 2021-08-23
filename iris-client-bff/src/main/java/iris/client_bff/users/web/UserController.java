package iris.client_bff.users.web;

import static iris.client_bff.users.web.UserMappers.*;

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
	private static final String FIELD_USER_ID = "userId";
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
		return map(userService.create(userInsertValidated));
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserDTO updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
		if (!ValidationHelper.isUUIDInputValid(id.toString(), FIELD_USER_ID)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		UserUpdateDTO userUpdateDTOValidated = validateUserUpdateDTO(userUpdateDTO);

		return map(userService.update(id, userUpdateDTOValidated));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable UUID id, Principal principal) {

		if (ValidationHelper.isUUIDInputValid(id.toString(), FIELD_USER_ID)) {
			this.userService.deleteById(id, principal.getName());
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}
	}

	private UserUpdateDTO validateUserUpdateDTO(UserUpdateDTO userUpdateDTO) {
		if (userUpdateDTO == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (validationHelper.isPossibleAttack(userUpdateDTO.getFirstName(), FIELD_FIRST_NAME, true)) {
			userUpdateDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (validationHelper.isPossibleAttack(userUpdateDTO.getLastName(), FIELD_LAST_NAME, true)) {
			userUpdateDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		var isInvalid = false;

		if (validationHelper.isPossibleAttackForRequiredValue(userUpdateDTO.getUserName(), FIELD_USER_NAME, false)
				|| validationHelper.isPossibleAttackForPassword(userUpdateDTO.getPassword(), FIELD_PASSWORD)) {
			isInvalid = true;
		}

		if (!(userUpdateDTO.getRole() == UserRoleDTO.ADMIN || userUpdateDTO.getRole() == UserRoleDTO.USER)) {
			log.warn(ErrorMessages.INVALID_INPUT + FIELD_ROLE + userUpdateDTO.getRole());
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (!validationHelper.isPasswordValid(userUpdateDTO.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					ValidationHelper.PW_ERROR_MESSAGE);
		}

		return userUpdateDTO;
	}

	private UserInsertDTO validateUserInsertDTO(UserInsertDTO userInsertDTO) {
		if (userInsertDTO == null) {
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
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					ValidationHelper.PW_ERROR_MESSAGE);
		}

		return userInsertDTO;
	}

}
