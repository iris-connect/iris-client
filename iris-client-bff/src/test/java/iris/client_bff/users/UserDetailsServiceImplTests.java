package iris.client_bff.users;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
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
class UserDetailsServiceImplTests {

	@Mock
	UserAccountsRepository userAccountsRepository;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Mock
	JWTService jwtService;

	UserDetailsServiceImpl userDetailsService;

	UserAccountIdentifier notFound = UserAccountIdentifier.of(UUID.randomUUID());

	UserAccount admin = userAccountAdmin();
	UserAccount user = spy(userAccountUser());

	UserAccountAuthentication adminAuth = new UserAccountAuthentication(admin, true,
			List.of(new SimpleGrantedAuthority("ADMIN")));
	UserAccountAuthentication userAuth = new UserAccountAuthentication(user, true,
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

		assertThrows(RuntimeException.class, () -> userDetailsService.update(admin.getId(), null, userAuth));
	}

	@Test
	void fails_notAdmin_changeUserName() {

		mockUserFound();

		var dto = UserUpdateDTO.builder().userName("new").build();

		assertThrows(RuntimeException.class, () -> userDetailsService.update(user.getId(), dto, userAuth));
	}

	@Test
	void fails_notAdmin_changeRole() {

		mockUserFound();

		var dto = UserUpdateDTO.builder().role(UserRoleDTO.ADMIN).build();

		assertThrows(RuntimeException.class, () -> userDetailsService.update(user.getId(), dto, userAuth));
	}

	@Test
	void fails_lastAdmin_changeRole() {

		mockAdminFound();
		mockCountLastAdmin();

		var dto = UserUpdateDTO.builder().role(UserRoleDTO.USER).build();

		assertThrows(RuntimeException.class, () -> userDetailsService.update(admin.getId(), dto, adminAuth));
	}

	@Test
	void ok_admin_changeNothing() {

		mockUserFound();
		mockSaveUser();

		var dto = UserUpdateDTO.builder().build();

		userDetailsService.update(user.getId(), dto, adminAuth);

		verify(userAccountsRepository).save(user);
		verifyNoMoreInteractions(jwtService, userAccountsRepository);
	}

	@Test
	void ok_admin_changeAll() {

		mockUserFound();
		mockSaveUser();

		var dto = UserUpdateDTO.builder().firstName("fn").lastName("ln").userName("un").password("pw")
				.role(UserRoleDTO.ADMIN).build();

		var ret = userDetailsService.update(user.getId(), dto, adminAuth);

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

		var dto = UserUpdateDTO.builder().userName("un").build();

		userDetailsService.update(user.getId(), dto, adminAuth);

		verify(jwtService).invalidateTokensOfUser("tm");

		dto = UserUpdateDTO.builder().password("pw").build();

		userDetailsService.update(user.getId(), dto, adminAuth);

		verify(jwtService).invalidateTokensOfUser("un");

		dto = UserUpdateDTO.builder().role(UserRoleDTO.ADMIN).build();

		userDetailsService.update(user.getId(), dto, adminAuth);

		verify(jwtService, times(2)).invalidateTokensOfUser("un");
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
		verify(jwtService).invalidateTokensOfUser(userName);

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
