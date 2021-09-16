package iris.client_bff.auth.db;

import static org.mockito.ArgumentMatchers.*;

import iris.client_bff.users.UserAccountsRepository;
import iris.client_bff.users.entities.UserAccount;

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
	private UserAccountsRepository repo;

	@Mock
	private DbAuthProperties conf;

	@Mock
	private PasswordEncoder encoder;

	private InitialAdminLoader loader;

	@BeforeEach
	public void init() {
		loader = new InitialAdminLoader(conf, repo, encoder);
	}

	@Test
	void shouldCreateAnAdminUserIfItDoesNotExist() {

		// when
		Mockito.when(conf.getAdminUserName()).thenReturn("admin");
		Mockito.when(conf.getAdminUserPassword()).thenReturn("admin");
		Mockito.when(repo.findByUserName(eq("admin"))).thenReturn(Optional.empty());

		// then
		loader.createAdminUserIfNotExists();

		// assert
		Mockito.verify(repo).save(any());
		Mockito.verify(encoder).encode("admin");
		Mockito.verifyNoMoreInteractions(repo, conf, encoder);
	}

	@Test
	void shouldNotCreateAnAdminUserIfItDoesExist() {

		// when
		Mockito.when(conf.getAdminUserName()).thenReturn("admin");

		var existingUser = new UserAccount();
		existingUser.setUserName("admin");

		Mockito.when(repo.findByUserName(eq("admin"))).thenReturn(Optional.of(existingUser));

		// then
		loader.createAdminUserIfNotExists();

		// assert
		Mockito.verify(repo, Mockito.never()).save(any());
		Mockito.verifyNoMoreInteractions(repo, conf, encoder);
	}
}
