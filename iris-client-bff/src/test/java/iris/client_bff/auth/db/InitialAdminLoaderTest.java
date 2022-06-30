package iris.client_bff.auth.db;

import static org.mockito.ArgumentMatchers.*;

import iris.client_bff.auth.db.InitialAdminLoader.Properties;
import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserService;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class InitialAdminLoaderTest {

	@Mock
	private UserService userService;

	private InitialAdminLoader.Properties conf = new Properties("admin", "admin");

	@Mock
	private PasswordEncoder encoder;

	private InitialAdminLoader loader;

	@BeforeEach
	public void init() {
		loader = new InitialAdminLoader(conf, userService, encoder);
	}

	@Test
	void shouldCreateAnAdminUserIfItDoesNotExist() {

		// when
		Mockito.when(userService.findByUsername(eq("admin"))).thenReturn(Optional.empty());

		// then
		loader.createAdminUserIfNotExists();

		// assert
		Mockito.verify(userService).create(any());
		Mockito.verify(encoder).encode("admin");
		Mockito.verifyNoMoreInteractions(userService, encoder);
	}

	@Test
	void shouldNotCreateAnAdminUserIfItDoesExist() {

		var existingUser = new UserAccount();
		existingUser.setUserName("admin");

		Mockito.when(userService.findByUsername(eq("admin"))).thenReturn(Optional.of(existingUser));

		// then
		loader.createAdminUserIfNotExists();

		// assert
		Mockito.verify(userService, Mockito.never()).create(any());
		Mockito.verifyNoMoreInteractions(userService, encoder);
	}
}
