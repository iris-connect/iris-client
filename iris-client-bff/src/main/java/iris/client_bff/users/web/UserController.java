package iris.client_bff.users.web;

import static iris.client_bff.users.web.UserMappers.map;

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

	private static final String ERR_MSG_ROLE = " - role: ";
	private static final String ERR_MSG_PASSWORD = " - password";
	private static final String ERR_MSG_USER_NAME = " - userName: ";
	private static final String ERR_MSG_LAST_NAME = " - lastName";
	private static final String ERR_MSG_FIRST_NAME = " - firstName";
	private static final String ERR_MSG_USER_ID = " - userId: ";
	private final UserDetailsServiceImpl userService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserListDTO getAllUsers() {
		return new UserListDTO().users(this.userService.loadAll().stream().map(UserMappers::map).collect(Collectors.toList()));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserDTO createUser(@RequestBody @Valid UserInsertDTO userInsert) {
		UserInsertDTO userInsertValidated = validateUserInsertDTO(userInsert);
		return map(userService.create(userInsertValidated));
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserDTO updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
		if (!ValidationHelper.isUUIDInputValid(id.toString(), ERR_MSG_USER_ID)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		UserUpdateDTO userUpdateDTOValidated = validateUserUpdateDTO(userUpdateDTO);

		return map(userService.update(id, userUpdateDTOValidated));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable UUID id) {
		if (ValidationHelper.isUUIDInputValid(id.toString(), ERR_MSG_USER_ID)) {
			this.userService.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}
	}

	private UserUpdateDTO validateUserUpdateDTO(UserUpdateDTO userUpdateDTO) {
		if (userUpdateDTO == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (ValidationHelper.isPossibleAttack(userUpdateDTO.getFirstName(), ERR_MSG_FIRST_NAME)) {
			userUpdateDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(userUpdateDTO.getLastName(), ERR_MSG_LAST_NAME)) {
			userUpdateDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper
			.isPossibleAttackForRequiredValue(userUpdateDTO.getUserName(), ERR_MSG_USER_NAME + userUpdateDTO.getUserName())) {
			userUpdateDTO.setUserName(ErrorMessages.INVALID_INPUT_STRING);
		}

		boolean isInvalid = false;

		if (ValidationHelper.isPossibleAttackForRequiredValue(userUpdateDTO.getPassword(), ERR_MSG_PASSWORD)) {
			isInvalid = true;
		}

		if (!(userUpdateDTO.getRole() == UserRoleDTO.ADMIN || userUpdateDTO.getRole() == UserRoleDTO.USER)) {
			log.warn(ErrorMessages.INVALID_INPUT + ERR_MSG_ROLE + userUpdateDTO.getRole());
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		return userUpdateDTO;
	}

	private UserInsertDTO validateUserInsertDTO(UserInsertDTO userInsertDTO) {
		if (userInsertDTO == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		if (ValidationHelper.isPossibleAttack(userInsertDTO.getFirstName(), ERR_MSG_FIRST_NAME)) {
			userInsertDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(userInsertDTO.getLastName(), ERR_MSG_LAST_NAME)) {
			userInsertDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper
			.isPossibleAttackForRequiredValue(userInsertDTO.getUserName(), ERR_MSG_USER_NAME + userInsertDTO.getUserName())) {
			userInsertDTO.setUserName(ErrorMessages.INVALID_INPUT_STRING);
		}

		boolean isInvalid = false;

		if (ValidationHelper.isPossibleAttackForRequiredValue(userInsertDTO.getPassword(), ERR_MSG_PASSWORD)) {
			isInvalid = true;
		}

		if (!(userInsertDTO.getRole() == UserRoleDTO.ADMIN || userInsertDTO.getRole() == UserRoleDTO.USER)) {
			log.warn(ErrorMessages.INVALID_INPUT + ERR_MSG_ROLE + userInsertDTO.getRole());
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}

		return userInsertDTO;
	}

}
