package iris.client_bff.users;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;
import iris.client_bff.users.web.dto.UserRoleDTO;
import iris.client_bff.users.web.dto.UserUpdateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Jens Kutzsche
 */
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTests {

	@Mock
	UserAccountsRepository userAccountsRepository;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Mock
	JWTService jwtService;

	UserDetailsServiceImpl userDetailsService;

	UUID notFound = UUID.randomUUID();
	UUID foundAdmin = UUID.randomUUID();
	UUID foundUser = UUID.randomUUID();

	UserAccount admin = userAccountAdmin();
	UserAccount user = spy(userAccountUser());

	UserAccountAuthentication adminAuth = new UserAccountAuthentication("mm", true,
			List.of(new SimpleGrantedAuthority("ADMIN")));
	UserAccountAuthentication userAuth = new UserAccountAuthentication("tm", true,
			List.of(new SimpleGrantedAuthority("USER")));

	@BeforeEach
	public void init() {
		userDetailsService = new UserDetailsServiceImpl(userAccountsRepository, passwordEncoder, jwtService);
		reset(user);
	}

	@Test
	void fails_uuidNotFound() {

		mockUserNotFound();

		assertThrows(RuntimeException.class, () -> userDetailsService.update(notFound, null, null));
	}

	@Test
	void fails_notAdmin_changeOtherProfil() {

		mockAdminFound();

		assertThrows(RuntimeException.class, () -> userDetailsService.update(foundAdmin, null, userAuth));
	}

	@Test
	void fails_notAdmin_changeUserName() {

		mockUserFound();

		var dto = new UserUpdateDTO().userName("new");

		assertThrows(RuntimeException.class, () -> userDetailsService.update(foundUser, dto, userAuth));
	}

	@Test
	void fails_notAdmin_changeRole() {

		mockUserFound();

		var dto = new UserUpdateDTO().role(UserRoleDTO.ADMIN);

		assertThrows(RuntimeException.class, () -> userDetailsService.update(foundUser, dto, userAuth));
	}

	@Test
	void fails_lastAdmin_changeRole() {

		mockAdminFound();
		mockCountLastAdmin();

		var dto = new UserUpdateDTO().role(UserRoleDTO.USER);

		assertThrows(RuntimeException.class, () -> userDetailsService.update(foundAdmin, dto, adminAuth));
	}

	@Test
	void ok_admin_changeNothing() {

		mockUserFound();
		mockSaveUser();

		var dto = new UserUpdateDTO();

		userDetailsService.update(foundUser, dto, adminAuth);

		verify(user).getUserName();
		verify(user).getRole();
		verify(userAccountsRepository).save(user);
		verifyNoMoreInteractions(user, jwtService, userAccountsRepository);
	}

	@Test
	void ok_admin_changeAll() {

		mockUserFound();
		mockSaveUser();

		var dto = new UserUpdateDTO().firstName("fn").lastName("ln").userName("un").password("pw").role(UserRoleDTO.ADMIN);

		var ret = userDetailsService.update(foundUser, dto, adminAuth);

		assertThat(ret).extracting("firstName", "lastName", "userName", "role")
				.containsExactly("fn", "ln", "un", UserRole.ADMIN);
		assertThat(ret).extracting(UserAccount::getPassword).matches(it -> passwordEncoder.matches("pw", it));

		verify(jwtService).invalidateTokensOfUser("tm");
		verify(userAccountsRepository).save(user);
		verifyNoMoreInteractions(jwtService, userAccountsRepository);
	}

	@Test
	void ok_tokenInvalidateOnRigthChanges() {

		mockUserFound();
		mockSaveUser();

		var dto = new UserUpdateDTO().userName("un");

		userDetailsService.update(foundUser, dto, adminAuth);

		verify(jwtService).invalidateTokensOfUser("tm");

		dto = new UserUpdateDTO().password("pw");

		userDetailsService.update(foundUser, dto, adminAuth);

		verify(jwtService).invalidateTokensOfUser("un");

		dto = new UserUpdateDTO().role(UserRoleDTO.ADMIN);

		userDetailsService.update(foundUser, dto, adminAuth);

		verify(jwtService, times(2)).invalidateTokensOfUser("un");
	}

	@Test
	void ok_isOldPasswordCorrect() {

		mockUserFound();

		var value = userDetailsService.isOldPasswordCorrect(foundUser, "password");

		assertTrue(value);

		verify(userAccountsRepository).findById(foundUser);
		verifyNoMoreInteractions(userAccountsRepository);
	}

	@Test
	void fails_isOldPasswordCorrect() {

		mockUserFound();

		var value = userDetailsService.isOldPasswordCorrect(foundUser, "pass");

		assertFalse(value);

		verify(userAccountsRepository).findById(foundUser);
		verifyNoMoreInteractions(userAccountsRepository);
	}

	private UserAccount userAccountAdmin() {

		var account = new UserAccount();
		account.setUser_id(foundAdmin);
		account.setFirstName("Max");
		account.setLastName("Muster");
		account.setUserName("mm");
		account.setPassword(passwordEncoder.encode("password"));
		account.setRole(UserRole.ADMIN);

		return account;
	}

	private UserAccount userAccountUser() {

		var account = new UserAccount();
		account.setUser_id(foundUser);
		account.setFirstName("Thomas");
		account.setLastName("Müller");
		account.setUserName("tm");
		account.setPassword(passwordEncoder.encode("password"));
		account.setRole(UserRole.USER);

		return account;
	}

	private void mockUserNotFound() {
		when(userAccountsRepository.findById(notFound)).thenReturn(Optional.empty());
	}

	private void mockAdminFound() {
		when(userAccountsRepository.findById(foundAdmin)).thenReturn(Optional.of(admin));
	}

	private void mockUserFound() {
		when(userAccountsRepository.findById(foundUser)).thenReturn(Optional.of(user));
	}

	private void mockCountLastAdmin() {
		when(userAccountsRepository.countByRole(UserRole.ADMIN)).thenReturn(1l);
	}

	private void mockSaveUser() {
		when(userAccountsRepository.save(any(UserAccount.class))).then(AdditionalAnswers.returnsFirstArg());
	}
}
