package iris.client_bff.users.web;

import static iris.client_bff.users.web.UserMappers.map;

import iris.client_bff.core.security.InputValidationUtility;
import iris.client_bff.ui.messages.ErrorMessages;
import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.web.dto.UserDTO;
import iris.client_bff.users.web.dto.UserInsertDTO;
import iris.client_bff.users.web.dto.UserListDTO;
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

	private final InputValidationUtility inputValidationUtility;
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
		if (isUserInsertDTOInputValid(userInsert)) {
			return map(userService.create(userInsert));
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + ": " + userInsert);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ADMIN')")
	public UserDTO updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
		if (isUserUpdateDTOInputValid(userUpdateDTO)) {
			return map(userService.update(id, userUpdateDTO));
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + ": " + userUpdateDTO);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable UUID id) {
		if (inputValidationUtility.isUUIDInputValid(id.toString())) {
			this.userService.deleteById(id);
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + ": " + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	private boolean isUserUpdateDTOInputValid(UserUpdateDTO userUpdateDTO) {
		if (userUpdateDTO != null && userUpdateDTO.getFirstName() != null) {
			if (!inputValidationUtility.checkInputNameConventions(userUpdateDTO.getFirstName())
				|| !inputValidationUtility.checkInputForAttacks(userUpdateDTO.getFirstName())) {
				return false;
			}
		}

		if (userUpdateDTO != null && userUpdateDTO.getLastName() != null) {
			if (!inputValidationUtility.checkInputNameConventions(userUpdateDTO.getLastName())
				|| !inputValidationUtility.checkInputForAttacks(userUpdateDTO.getLastName())) {
				return false;
			}
		}

		if (userUpdateDTO != null) {
			if (!inputValidationUtility.checkInputForAttacks(userUpdateDTO.getUserName())) {
				return false;
			}
		}

		return true;
	}

	private boolean isUserInsertDTOInputValid(UserInsertDTO userInsertDTO) {
		if (userInsertDTO != null && userInsertDTO.getFirstName() != null) {
			if (!inputValidationUtility.checkInputNameConventions(userInsertDTO.getFirstName())
				|| !inputValidationUtility.checkInputForAttacks(userInsertDTO.getFirstName())) {
				return false;
			}
		}

		if (userInsertDTO != null && userInsertDTO.getLastName() != null) {
			if (!inputValidationUtility.checkInputNameConventions(userInsertDTO.getLastName())
				|| !inputValidationUtility.checkInputForAttacks(userInsertDTO.getLastName())) {
				return false;
			}
		}

		if (userInsertDTO != null) {
			if (!inputValidationUtility.checkInputForAttacks(userInsertDTO.getUserName())) {
				return false;
			}
		}

		return true;
	}

}
