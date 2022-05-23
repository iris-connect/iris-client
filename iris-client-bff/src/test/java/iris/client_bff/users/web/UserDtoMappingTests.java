package iris.client_bff.users.web;

import static iris.client_bff.core.model.MetadataUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.IrisMapperTest;
import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserRole;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.javafaker.Faker;

/**
 * @author Jens Kutzsche
 */
@IrisMapperTest(UserMapperImpl.class)
@DisplayName("Entity ⇔ DTO mapping ⇒ User")
@RequiredArgsConstructor
class UserDtoMappingTests {

	@MockBean
	UserService userService;

	@MockBean
	PasswordEncoder passwordEncoder;

	final UserMapper mapper;

	final Faker faker = Faker.instance();

	@Test
	@DisplayName("UserAccount ⇒ UserDTO")
	void mapping_fromUserAccount_toUserDTO() {

		when(userService.findByUuid(any()))
				.thenReturn(
						Optional.of(new UserAccount("admin", "admin", "admin", "admin", UserRole.ADMIN, false, null, null, false)));
		when(passwordEncoder.encode(any())).thenAnswer(it -> it.getArgument(0));

		var firstName = faker.name().firstName();
		var lastName = faker.name().lastName();
		var userName = faker.name().username();
		var createdAt = Instant.now().minus(7, ChronoUnit.DAYS);
		var lastModified = Instant.now();
		var userUuid = UUID.randomUUID();

		var user = new UserAccount();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserName(userName);
		user.setRole(UserRole.USER);

		setMetadata(user, createdAt, lastModified, userUuid, userUuid);

		var dto = mapper.toDto(user);

		var expected = UserDtos.Output.builder()
				.id(user.getId().toString())
				.firstName(firstName)
				.lastName(lastName)
				.userName(userName)
				.role(UserDtos.Role.USER)
				.createdAt(createdAt)
				.lastModifiedAt(lastModified)
				.createdBy("admin admin")
				.lastModifiedBy("admin admin")
				.build();

		assertThat(dto).isEqualTo(expected);
	}
}
