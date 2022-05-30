package iris.client_bff.users;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Jens Kutzsche
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

	@Mock
	UserAccountsRepository userAccountsRepository;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	UserService userDetailsService;

	UserAccountIdentifier notFound = UserAccountIdentifier.of(UUID.randomUUID());

	UserAccount admin = userAccountAdmin();
	UserAccount user = spy(userAccountUser());

	UserAccountAuthentication adminAuth = new UserAccountAuthentication(admin, true,
			List.of(new SimpleGrantedAuthority("ADMIN")));
	UserAccountAuthentication userAuth = new UserAccountAuthentication(user, true,
			List.of(new SimpleGrantedAuthority("USER")));

	@BeforeEach
	public void init() {
		userDetailsService = new UserService(userAccountsRepository, passwordEncoder);
		reset(user);
	}

	@Test
	void fails_uuidNotFound() {

		mockUserNotFound();

		assertThrows(RuntimeException.class,
				() -> userDetailsService.update(notFound, null, null, null, null, null, null, null));
	}

	@Test
	void fails_notAdmin_changeOtherProfil() {

		mockAdminFound();

		assertThrows(RuntimeException.class,
				() -> userDetailsService.update(admin.getId(), userAuth, null, null, null, null, null, null));
	}

	@Test
	void fails_notAdmin_changeUserName() {

		mockUserFound();

		assertThrows(RuntimeException.class,
				() -> userDetailsService.update(user.getId(), userAuth, null, null, "new", null, null, null));
	}

	@Test
	void fails_notAdmin_changeRole() {

		mockUserFound();

		assertThrows(RuntimeException.class,
				() -> userDetailsService.update(user.getId(), userAuth, null, null, null, null, UserRole.ADMIN, null));
	}

	@Test
	void fails_lastAdmin_changeRole() {

		mockAdminFound();
		mockCountLastAdmin();

		assertThrows(RuntimeException.class,
				() -> userDetailsService.update(admin.getId(), adminAuth, null, null, null, null, UserRole.USER, null));
	}

	@Test
	void ok_admin_changeNothing() {

		mockUserFound();
		mockSaveUser();

		userDetailsService.update(user.getId(), adminAuth, null, null, null, null, null, null);

		verify(userAccountsRepository).save(user);
		verify(user, times(2)).getId();
		verify(user).getUserName();
		verifyNoMoreInteractions(user, userAccountsRepository);
	}

	@Test
	void ok_admin_changeAll() {

		mockUserFound();
		mockSaveUser();

		var ret = userDetailsService.update(user.getId(), adminAuth, "ln", "fn", "un", "pw", UserRole.ADMIN, Boolean.TRUE);

		assertThat(ret).extracting("firstName", "lastName", "userName", "role")
				.containsExactly("fn", "ln", "un", UserRole.ADMIN);
		assertThat(ret).extracting(UserAccount::getPassword).matches(it -> passwordEncoder.matches("pw", it));

		verify(user).markLoginIncompatiblyUpdated();
		verify(userAccountsRepository).save(user);
		verifyNoMoreInteractions(userAccountsRepository);
	}

	@Test
	void ok_tokenInvalidateOnRigthChanges() {

		mockUserFound();
		mockSaveUser();

		userDetailsService.update(user.getId(), adminAuth, null, null, "un", null, null, null);

		verify(user).markLoginIncompatiblyUpdated();

		userDetailsService.update(user.getId(), adminAuth, null, null, null, "pw", null, null);

		verify(user, times(2)).markLoginIncompatiblyUpdated();

		userDetailsService.update(user.getId(), adminAuth, null, null, null, null, UserRole.ADMIN, null);

		verify(user, times(3)).markLoginIncompatiblyUpdated();
	}

	@Test
	void ok_isOldPasswordCorrect() {

		mockUserFound();

		var value = userDetailsService.isOldPasswordCorrect(user.getId(), "password");

		assertTrue(value);

		verify(userAccountsRepository).findUserById(user.getId());
		verifyNoMoreInteractions(userAccountsRepository);
	}

	@Test
	void fails_isOldPasswordCorrect() {

		mockUserFound();

		var value = userDetailsService.isOldPasswordCorrect(user.getId(), "pass");

		assertFalse(value);

		verify(userAccountsRepository).findUserById(user.getId());
		verifyNoMoreInteractions(userAccountsRepository);
	}

	@Test // for iris-backlog#235
	void ok_deleteById() {

		mockUserFound();
		var userCaptor = ArgumentCaptor.forClass(UserAccount.class);

		var id = user.getId();
		var userName = user.getUserName();

		userDetailsService.deleteById(id, "other");

		verify(userAccountsRepository).findUserById(id);
		verify(userAccountsRepository).save(userCaptor.capture());

		var user = userCaptor.getValue();
		assertThat(user.getUserName()).isNotEqualTo(userName);
		assertThat(user.getDeletedAt()).isNotNull();
		assertThat(user.getId()).isEqualTo(id);

		verifyNoMoreInteractions(userAccountsRepository);
	}

	private UserAccount userAccountAdmin() {

		var account = new UserAccount();
		account.setFirstName("Max");
		account.setLastName("Muster");
		account.setUserName("mm");
		account.setPassword(passwordEncoder.encode("password"));
		account.setRole(UserRole.ADMIN);

		return account;
	}

	private UserAccount userAccountUser() {

		var account = new UserAccount();
		account.setFirstName("Thomas");
		account.setLastName("Müller");
		account.setUserName("tm");
		account.setPassword(passwordEncoder.encode("password"));
		account.setRole(UserRole.USER);

		return account;
	}

	private void mockUserNotFound() {
		when(userAccountsRepository.findUserById(notFound)).thenReturn(Optional.empty());
	}

	private void mockAdminFound() {
		when(userAccountsRepository.findUserById(admin.getId())).thenReturn(Optional.of(admin));
	}

	private void mockUserFound() {
		when(userAccountsRepository.findUserById(user.getId())).thenReturn(Optional.of(user));
	}

	private void mockCountLastAdmin() {
		when(userAccountsRepository.countUsersByRole(UserRole.ADMIN)).thenReturn(1l);
	}

	private void mockSaveUser() {
		when(userAccountsRepository.save(any(UserAccount.class))).then(AdditionalAnswers.returnsFirstArg());
	}
}
