package iris.client_bff.users;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.config.HealthDepartmentProperties;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;
import iris.client_bff.users.web.UserController;
import iris.client_bff.users.web.dto.UserInsertDTO;
import iris.client_bff.users.web.dto.UserRoleDTO;
import iris.client_bff.users.web.dto.UserUpdateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Jens Kutzsche
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTests {

	@Mock(lenient = true)
	UserDetailsServiceImpl userService;
	@Mock(lenient = true)
	AlertService alertService;
	@Mock(lenient = true)
	HealthDepartmentProperties hdConfig;

	@InjectMocks
	ValidationHelper validationHelper;

	UserController userController;

	@BeforeEach
	public void init() {

		when(hdConfig.getAbbreviation()).thenReturn("1A9");
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
		when(userService.update(any(UUID.class), any(UserUpdateDTO.class), any(UserAccountAuthentication.class)))
				.thenAnswer(it -> {
					var user = it.getArgument(1, UserUpdateDTO.class);
					var account = new UserAccount();
					account.setFirstName(user.getFirstName());
					account.setLastName(user.getLastName());
					account.setPassword(user.getPassword());
					account.setUserName(user.getUserName());
					account.setRole(UserRole.valueOf(user.getRole().name()));
					return account;
				});
		when(userService.isOldPasswordCorrect(any(UUID.class), anyString())).thenReturn(true);

		userController = new UserController(userService, validationHelper);
	}

	@ParameterizedTest
	@ValueSource(
			strings = { "Password", "X1aba2a", "X1aba2ae+", "X1ab€2 ae", "X1ab€2 ae", "X1ab€2 ae" })
	void testWrongPasswords(String pw) {

		var dto = new UserInsertDTO().firstName("fn").lastName("ln").userName("un").password(pw).role(UserRoleDTO.USER);
		Assertions.assertThrows(ResponseStatusException.class, () -> userController.createUser(dto));

		var dto2 = new UserUpdateDTO().firstName("fn1").lastName("ln1").userName("un").password(pw).oldPassword(pw)
				.role(UserRoleDTO.USER);
		var authentication = new UserAccountAuthentication("fn", true,
				List.of(new SimpleGrantedAuthority(UserRole.USER.name())));

		Assertions.assertThrows(ResponseStatusException.class,
				() -> userController.updateUser(UUID.randomUUID(), dto2, authentication));
	}

	@ParameterizedTest
	@ValueSource(strings = { "Password12A_", "X1ab__ae", "X1aba2ae", "X1ab$aae", "x-ab$aae", "X-AB$AAE" })
	void testCorrectPasswords(String pw) {

		var dto = new UserInsertDTO().firstName("fn").lastName("ln").userName("un").password(pw).role(UserRoleDTO.USER);
		var user = userController.createUser(dto);

		verify(userService).create(dto);
		assertThat(user).isNotNull();

		var dto2 = new UserUpdateDTO().firstName("fn1").lastName("ln1").userName("un").password(pw).oldPassword(pw)
				.role(UserRoleDTO.USER);
		var authentication = new UserAccountAuthentication("fn", true,
				List.of(new SimpleGrantedAuthority(UserRole.USER.name())));
		var id = UUID.randomUUID();

		var account = new UserAccount();
		account.setUser_id(id);
		account.setFirstName("fn");
		account.setLastName("ln");
		account.setPassword(pw);
		account.setUserName("un");
		account.setRole(UserRole.USER);

		when(userService.findByUsername(anyString())).thenReturn(Optional.of(account));

		userController.updateUser(id, dto2, authentication);

		verify(userService).update(id, dto2, authentication);
		assertThat(user).isNotNull();
	}

	@Test
	void testNewUserNameExist() {

		var dto = new UserUpdateDTO().firstName("fn1").lastName("ln1").userName("un").password("abcde123")
				.oldPassword("abcde123").role(UserRoleDTO.USER);
		var authentication = new UserAccountAuthentication("test", true,
				List.of(new SimpleGrantedAuthority(UserRole.ADMIN.name())));

		var account = new UserAccount();
		account.setUser_id(UUID.randomUUID());
		account.setFirstName("fn");
		account.setLastName("ln");
		account.setPassword("abcde123");
		account.setUserName("un");
		account.setRole(UserRole.USER);

		when(userService.findByUsername(anyString())).thenReturn(Optional.of(account));

		Assertions.assertThrows(ResponseStatusException.class,
				() -> userController.updateUser(UUID.randomUUID(), dto, authentication));
	}

	@Test
	void testRootChangePW_ownWithOldPassword() {

		var dto = new UserUpdateDTO().password("abcde1234").role(UserRoleDTO.ADMIN);
		var authentication = new UserAccountAuthentication("test", true,
				List.of(new SimpleGrantedAuthority(UserRole.ADMIN.name())));

		var id = UUID.randomUUID();

		var account = new UserAccount();
		account.setUser_id(id);
		account.setFirstName("fn");
		account.setLastName("ln");
		account.setPassword("abcde123");
		account.setUserName("un");
		account.setRole(UserRole.ADMIN);

		when(userService.findByUsername(anyString())).thenReturn(Optional.of(account));
		when(userService.isItCurrentUser(any(UUID.class), any(UserAccountAuthentication.class)))
				.thenReturn(true);

		Assertions.assertThrows(ResponseStatusException.class,
				() -> userController.updateUser(id, dto, authentication));

		var dto2 = dto.oldPassword("abcde123");
		Assertions.assertDoesNotThrow(() -> userController.updateUser(id, dto2, authentication));
	}

	@Test
	void testRootChangePW_foreignWithoutOldPassword() {

		var dto = new UserUpdateDTO().password("abcde123").role(UserRoleDTO.ADMIN);
		var authentication = new UserAccountAuthentication("test", true,
				List.of(new SimpleGrantedAuthority(UserRole.ADMIN.name())));

		var id = UUID.randomUUID();

		var account = new UserAccount();
		account.setUser_id(id);
		account.setFirstName("fn");
		account.setLastName("ln");
		account.setPassword("abcde123");
		account.setUserName("un");
		account.setRole(UserRole.ADMIN);

		when(userService.findByUsername(anyString())).thenReturn(Optional.of(account));
		when(userService.isItCurrentUser(any(UUID.class), any(UserAccountAuthentication.class)))
				.thenReturn(false);

		Assertions.assertDoesNotThrow(() -> userController.updateUser(id, dto, authentication));
	}
}
