package iris.client_bff.users;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.config.HealthDepartmentConfig;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;
import iris.client_bff.users.web.UserController;
import iris.client_bff.users.web.dto.UserInsertDTO;
import iris.client_bff.users.web.dto.UserRoleDTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Jens Kutzsche
 */
@ExtendWith(MockitoExtension.class)
class UserControlerTests {

	@Mock(lenient = true)
	UserDetailsServiceImpl userService;
	@Mock(lenient = true)
	AlertService alertService;
	@Mock(lenient = true)
	HealthDepartmentConfig hdConfig;

	@InjectMocks
	ValidationHelper validationHelper;

	UserController userController;

	@BeforeEach
	public void init() {

		when(hdConfig.getZipCode()).thenReturn("01665");
		when(userService.create(any(UserInsertDTO.class))).thenAnswer(it -> {
			var user = it.getArgument(0, UserInsertDTO.class);
			var account = new UserAccount();
			account.setFirstName(user.getFirstName());
			account.setLastName(user.getLastName());
			account.setPassword(user.getPassword());
			account.setUserName(user.getUserName());
			account.setRole(UserRole.valueOf(user.getRole().name()));
			return account;
		});

		userController = new UserController(userService, validationHelper);
	}

	@ParameterizedTest
	@ValueSource(
			strings = { "Password", "X1ab€2a", "X1ab€aae", "X1aba2ae", "x1ab€2ae", "X1ab€2 ae", "X1ab€2 ae", "X1ab€2 ae" })
	void testWrongPasswords(String pw) {

		var dto = new UserInsertDTO().firstName("fn").lastName("ln").userName("un").password(pw).role(UserRoleDTO.USER);

		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userController.createUser(dto);
		}, ValidationHelper.PW_ERROR_MESSAGE);
	}

	@ParameterizedTest
	@ValueSource(strings = { "Password12A_", "X1ab_2ae" })
	void testCorrectPasswords(String pw) {

		var dto = new UserInsertDTO().firstName("fn").lastName("ln").userName("un").password(pw).role(UserRoleDTO.USER);
		var user = userController.createUser(dto);

		assertThat(user).isNotNull();
	}
}
