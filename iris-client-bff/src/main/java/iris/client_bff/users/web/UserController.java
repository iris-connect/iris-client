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

	private static final String EXCEPTION_MESSAGE_ROLE = " - role: ";
	private static final String EXCEPTION_MESSAGE_PASSWORD = " - password";
	private static final String EXCEPTION_MESSAGE_USER_NAME = " - userName: ";
	private static final String EXCEPTION_MESSAGE_LAST_NAME = " - lastName: ";
	private static final String EXCEPTION_MESSAGE_FIRST_NAME = " - firstName: ";
	private static final String EXCEPTION_MESSAGE_ID = " - id: ";
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
		UserInsertDTO userInsertValidated = userInsertDTOInputValidated(userInsert);
		return map(userService.create(userInsertValidated));
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserDTO updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
		if (ValidationHelper.isUUIDInputValid(id.toString())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_ID + userUpdateDTO);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		UserUpdateDTO userUpdateDTOValidated = userUpdateDTOInputValidated(userUpdateDTO);

		return map(userService.update(id, userUpdateDTOValidated));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable UUID id) {
		if (ValidationHelper.isUUIDInputValid(id.toString())) {
			this.userService.deleteById(id);
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_ID + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	private UserUpdateDTO userUpdateDTOInputValidated(UserUpdateDTO userUpdateDTO) {
		if (userUpdateDTO == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (userUpdateDTO.getFirstName() != null && !ValidationHelper.checkInputForAttacks(userUpdateDTO.getFirstName())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_FIRST_NAME + userUpdateDTO.getFirstName());
			userUpdateDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (userUpdateDTO.getLastName() != null && !ValidationHelper.checkInputForAttacks(userUpdateDTO.getLastName())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_LAST_NAME + userUpdateDTO.getFirstName());
			userUpdateDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (!ValidationHelper.checkInputForAttacks(userUpdateDTO.getUserName())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_USER_NAME + userUpdateDTO.getFirstName());
			userUpdateDTO.setUserName(ErrorMessages.INVALID_INPUT_STRING);
		}

		boolean isInvalid = false;

		if (!ValidationHelper.checkInputForAttacks(userUpdateDTO.getPassword())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_PASSWORD);
			isInvalid = true;
		}

		if (!(userUpdateDTO.getRole() == UserRoleDTO.ADMIN || userUpdateDTO.getRole() == UserRoleDTO.USER)) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_ROLE + userUpdateDTO.getRole());
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return userUpdateDTO;
	}

	private UserInsertDTO userInsertDTOInputValidated(UserInsertDTO userInsertDTO) {
		if (userInsertDTO == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (userInsertDTO.getFirstName() != null && !ValidationHelper.checkInputForAttacks(userInsertDTO.getFirstName())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_FIRST_NAME + userInsertDTO.getFirstName());
			userInsertDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (userInsertDTO.getLastName() != null && !ValidationHelper.checkInputForAttacks(userInsertDTO.getLastName())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_LAST_NAME + userInsertDTO.getFirstName());
			userInsertDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (!ValidationHelper.checkInputForAttacks(userInsertDTO.getUserName())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_USER_NAME + userInsertDTO.getFirstName());
			userInsertDTO.setUserName(ErrorMessages.INVALID_INPUT_STRING);
		}

		boolean isInvalid = false;

		if (!ValidationHelper.checkInputForAttacks(userInsertDTO.getPassword())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_PASSWORD);
			isInvalid = true;
		}

		if (!(userInsertDTO.getRole() == UserRoleDTO.ADMIN || userInsertDTO.getRole() == UserRoleDTO.USER)) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_ROLE + userInsertDTO.getRole());
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return userInsertDTO;
	}

}
