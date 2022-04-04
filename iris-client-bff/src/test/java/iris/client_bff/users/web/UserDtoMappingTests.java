package iris.client_bff.users.web;

import static iris.client_bff.core.MetadataUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.core.AggregateMapperImpl;
import iris.client_bff.core.ConversionServiceAdapter;
import iris.client_bff.core.MetadataMapperImpl;
import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;
import iris.client_bff.users.web.dto.UserDTO;
import iris.client_bff.users.web.dto.UserRoleDTO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

/**
 * @author Jens Kutzsche
 */
@ExtendWith(MockitoExtension.class)
@Tag("mappings")
@DisplayName("Entity ⇔ DTO mapping ⇒ User")
class UserDtoMappingTests {

	@Mock
	UserDetailsServiceImpl userService;

	@InjectMocks
	MetadataMapperImpl metadataMapper;

	@Mock
	ConversionServiceAdapter conversion;

	UserMapperImpl mapper;

	final Faker faker = Faker.instance();

	@BeforeEach
	void init() {
		mapper = new UserMapperImpl(conversion, metadataMapper);
	}

	@Test
	@DisplayName("UserAccount ⇒ UserDTO")
	void mapping_fromUserAccount_toUserDTO() {

		when(userService.findByUuid(any()))
				.thenReturn(Optional.of(new UserAccount("admin", "admin", "admin", "admin", UserRole.ADMIN)));
		when(conversion.mapIdToString(any())).thenAnswer(it -> new AggregateMapperImpl().convert(it.getArgument(0)));

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

		var expected = UserDTO.builder()
				.id(user.getId().toString())
				.firstName(firstName)
				.lastName(lastName)
				.userName(userName)
				.role(UserRoleDTO.USER)
				.createdAt(createdAt)
				.lastModifiedAt(lastModified)
				.createdBy("admin admin")
				.lastModifiedBy("admin admin")
				.build();

		assertThat(dto).isEqualTo(expected);
	}
}
