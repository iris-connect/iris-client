package iris.client_bff.users.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.config.CentralConfigurationService;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;
import iris.client_bff.users.UserRole;
import iris.client_bff.users.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
	UserService userService;
	@Mock(lenient = true)
	AlertService alertService;
	@Mock(lenient = true)
	CentralConfigurationService hdConfig;
	@Mock(lenient = true)
	UserMapperImpl userMapper;

	UserController userController;

	@BeforeEach
	public void init() {

		when(hdConfig.getAbbreviation()).thenReturn("1A9");
		when(userService.create(any(UserAccount.class))).thenAnswer(it -> it.getArgument(0, UserAccount.class));
		when(userService.isOldPasswordCorrect(any(UserAccountIdentifier.class), anyString())).thenReturn(true);

		userController = new UserController(userService, userMapper);
	}

	@Test
	void testNewUserNameExist() {

		var dto = UserDtos.Update.builder().firstName("fn1").lastName("ln1").userName("un").password("abcde123")
				.oldPassword("abcde123").role(UserDtos.Role.USER).build();
		var authentication = createAuthentication("test");

		var account = new UserAccount();
		account.setFirstName("fn");
		account.setLastName("ln");
		account.setPassword("abcde123");
		account.setUserName("un");
		account.setRole(UserRole.USER);

		when(userService.findByUsername(anyString())).thenReturn(Optional.of(account));

		assertThrows(ResponseStatusException.class,
				() -> userController.updateUser(UserAccountIdentifier.of(UUID.randomUUID()), dto, authentication));
	}

	@Test
	void testRootChangePW_ownWithOldPassword() {

		var dto = UserDtos.Update.builder().password("abcde1234").role(UserDtos.Role.ADMIN).build();
		var authentication = createAuthentication("test");

		var account = new UserAccount();
		account.setFirstName("fn");
		account.setLastName("ln");
		account.setPassword("abcde123");
		account.setUserName("un");
		account.setRole(UserRole.ADMIN);

		when(userService.findByUsername(anyString())).thenReturn(Optional.of(account));
		when(userService.isItCurrentUser(any(UserAccountIdentifier.class), any(UserAccountAuthentication.class)))
				.thenReturn(true);

		Assertions.assertThrows(ResponseStatusException.class,
				() -> userController.updateUser(account.getId(), dto, authentication));

		var dto2 = UserDtos.Update.builder().password("abcde1234").oldPassword("abcde123").role(UserDtos.Role.ADMIN)
				.build();
		assertDoesNotThrow(() -> userController.updateUser(account.getId(), dto2, authentication));
	}

	@Test
	void testRootChangePW_foreignWithoutOldPassword() {

		var dto = UserDtos.Update.builder().password("abcde123").role(UserDtos.Role.ADMIN).build();
		var authentication = createAuthentication("test");

		var account = new UserAccount();
		account.setFirstName("fn");
		account.setLastName("ln");
		account.setPassword("abcde123");
		account.setUserName("un");
		account.setRole(UserRole.ADMIN);

		when(userService.findByUsername(anyString())).thenReturn(Optional.of(account));
		when(userService.isItCurrentUser(any(UserAccountIdentifier.class), any(UserAccountAuthentication.class)))
				.thenReturn(false);

		assertDoesNotThrow(() -> userController.updateUser(account.getId(), dto, authentication));
	}

	private UserAccountAuthentication createAuthentication(String userName) {

		var user = new UserAccount().setUserName(userName);

		return new UserAccountAuthentication(user, true,
				List.of(new SimpleGrantedAuthority(UserRole.ADMIN.name())));
	}
}
