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
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - id: " + userUpdateDTO);
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
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - id: " + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	private UserUpdateDTO userUpdateDTOInputValidated(UserUpdateDTO userUpdateDTO) {
		boolean isInvalid = false;

		if (userUpdateDTO == null) {
			isInvalid = true;
		}

		if (userUpdateDTO != null && userUpdateDTO.getFirstName() != null) {
			if (!ValidationHelper.checkInputForAttacks(userUpdateDTO.getFirstName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - firstName: " + userUpdateDTO.getFirstName());
				userUpdateDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (userUpdateDTO != null && userUpdateDTO.getLastName() != null) {
			if (!ValidationHelper.checkInputForAttacks(userUpdateDTO.getLastName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - lastName: " + userUpdateDTO.getFirstName());
				userUpdateDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (userUpdateDTO != null) {
			if (!ValidationHelper.checkInputForAttacks(userUpdateDTO.getUserName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - userName: " + userUpdateDTO.getFirstName());
				userUpdateDTO.setUserName(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (!ValidationHelper.checkInputForAttacks(userUpdateDTO.getPassword())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - password");
				isInvalid = true;
			}

			if ((userUpdateDTO.getRole() == UserRoleDTO.ADMIN || userUpdateDTO.getRole() == UserRoleDTO.USER)) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - role: " + userUpdateDTO.getRole());
				isInvalid = true;
			}
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return userUpdateDTO;
	}

	private UserInsertDTO userInsertDTOInputValidated(UserInsertDTO userInsertDTO) {
		boolean isInvalid = false;

		if (userInsertDTO == null) {
			isInvalid = true;
		}

		if (userInsertDTO != null && userInsertDTO.getFirstName() != null) {
			if (!ValidationHelper.checkInputForAttacks(userInsertDTO.getFirstName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - firstName: " + userInsertDTO.getFirstName());
				userInsertDTO.setFirstName(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (userInsertDTO != null && userInsertDTO.getLastName() != null) {
			if (!ValidationHelper.checkInputForAttacks(userInsertDTO.getLastName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - lastName: " + userInsertDTO.getFirstName());
				userInsertDTO.setLastName(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (userInsertDTO != null) {
			if (!ValidationHelper.checkInputForAttacks(userInsertDTO.getUserName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - userName: " + userInsertDTO.getFirstName());
				userInsertDTO.setUserName(ErrorMessages.INVALID_INPUT_STRING);
			}

			if (!ValidationHelper.checkInputForAttacks(userInsertDTO.getPassword())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - password");
				isInvalid = true;
			}

			if ((userInsertDTO.getRole() == UserRoleDTO.ADMIN || userInsertDTO.getRole() == UserRoleDTO.USER)) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - role: " + userInsertDTO.getRole());
				isInvalid = true;
			}
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return userInsertDTO;
	}

}
