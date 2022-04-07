package iris.client_bff.users.web;

import static io.restassured.http.ContentType.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static iris.client_bff.users.web.UsersTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.HttpStatus.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.WithMockAdmin;
import iris.client_bff.matchers.IsUuid;
import iris.client_bff.users.UserAccountsRepository;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.github.javafaker.Faker;

@IrisWebIntegrationTest
@WithMockAdmin
@RequiredArgsConstructor
@Tag("users")
@Tag("rest-controller")
@DisplayName("IT of web controller for users management")
class UserControllerIntegrationTests {

	private static final String BASE_URL = "/users";
	private static final String DETAILS_URL = BASE_URL + "/{id}";

	final MockMvc mvc;
	final UserAccountsRepository users;

	Faker faker = Faker.instance();

	@BeforeEach
	void init() {

		Locale.setDefault(Locale.ENGLISH);

		RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
				.setMockMvc(mvc)
				.setContentType(JSON).build();
	}

	@AfterAll
	void cleanup() {

		// Must be done, otherwise other tests may break using RestAssured.
		RestAssuredMockMvc.requestSpecification = null;
	}

	@Test
	@WithMockUser
	@DisplayName("getAllUsers: endpoint 🔒")
	void getAllUsers_EndpointsProtected() {

		when()
				.get(BASE_URL)

				.then()
				.status(FORBIDDEN);
	}

	@Test
	@WithMockUser
	@DisplayName("createUser: endpoint 🔒")
	void createUser_EndpointProtected() {

		given()
				.body(VALID_USER.formatted(faker.name().username()))

				.when()
				.post(BASE_URL)

				.then()
				.status(FORBIDDEN);
	}

	@Test
	@WithAnonymousUser
	@DisplayName("updateUser: endpoint 🔒")
	void updateUser_EndpointProtected() {

		when()
				.patch(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(FORBIDDEN);
	}

	@Test
	@WithMockUser
	@DisplayName("deleteUser: endpoint 🔒")
	void deleteUser_EndpointProtected() {

		when()
				.delete(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(FORBIDDEN);
	}

	@Test
	@DisplayName("create user: valid JSON ⇒ 💾 user + 🔙 201 + new user")
	void createUser_ValidJson_ReturnsEncryptedTokens() throws Throwable {

		var count = users.count();
		var username = faker.name().username();

		given()
				.body(VALID_USER.formatted(username))

				.when()
				.post(BASE_URL)

				.then()
				.status(CREATED)
				.body(not(containsString("password")))
				.body("id", IsUuid.uuidString(),
						"firstName", is("first name"),
						"lastName", is("last name"),
						"userName", is(username),
						"role", is("USER"));

		assertThat(users.count()).isEqualTo(count + 1);
	}

	@Test
	@DisplayName("create user: without values ⇒ 💾 nothing + 🔙 validation errors")
	void createUser_WithoutValues_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body((String) null)

				.when()
				.post(BASE_URL)

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		given()
				.body(WITHOUT_VALUES)

				.when()
				.post(BASE_URL)

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("create user: with blank values ⇒ 💾 nothing + 🔙 validation errors")
	void createUser_WithBlankValues_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body(WITH_BLANK_VALUES)

				.when()
				.post(BASE_URL)

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("create user: with too long values ⇒ 💾 nothing + 🔙 validation errors")
	void createUser_WithTooLongValues_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body(WITH_TOO_LONG_VALUES)

				.when()
				.post(BASE_URL)

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("create user: with forbidden symbols ⇒ 💾 nothing + 🔙 validation errors")
	void createUser_WithForbiddenSymbols_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body(WITH_FORBIDDEN_SYMBOLS)

				.when()
				.post(BASE_URL)

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());
		// .body(allOf(
		// containsString("firstName: Contains illegal characters"),
		// containsString("lastName: Contains illegal characters"),
		// containsString("userName: Contains illegal characters"),
		// containsString("password: Contains illegal characters")));

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("create user: with invalid password ⇒ 💾 nothing + 🔙 validation errors")
	void createUser_WithInvalidPassword_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body(INVALID_PASSWORD)

				.when()
				.post(BASE_URL)

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("create user: save with existing username ⇒ 💾 nothing + 🔙 validation errors")
	void createUser_WithExistingUsername_ReturnsValidationErrors() {

		var count = users.count();
		var username = faker.name().username();

		given()
				.body(VALID_USER.formatted(username))

				.when()
				.post(BASE_URL)

				.then()
				.status(CREATED);

		given()
				.body(VALID_USER.formatted(username))

				.when()
				.post(BASE_URL)

				.then()
				.status(BAD_REQUEST);

		assertThat(users.count()).isEqualTo(count + 1);
	}

	@Test
	@DisplayName("update user: without values ⇒ 💾 unchanged user + 🔙 200 + unchanged user")
	void updateUser_WithoutValues_ChangeNothing() {

		var count = users.count();
		var admin = users.findByUserName("admin").get();

		given()
				.body(WITHOUT_VALUES)

				.when()
				.patch(DETAILS_URL, admin.getId())

				.then()
				.status(OK)
				.body(not(containsString("password")))
				.body("id", is(admin.getId().toString()),
						"firstName", is(admin.getFirstName()),
						"lastName", is(admin.getLastName()),
						"userName", is(admin.getUserName()),
						"role", is(admin.getRole().toString()));

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("update user: valid JSON ⇒ 💾 changed user + 🔙 200 + changed user")
	void updateUser_ValidJson_ChangeUser() {

		var count = users.count();
		var admin = users.findByUserName("admin").get();

		given()
				.body(VALID_UPDATE_ADMIN)

				.when()
				.patch(DETAILS_URL, admin.getId())

				.then()
				.status(OK)
				.body(not(containsString("password")))
				.body("id", is(admin.getId().toString()),
						"firstName", is("admin Test"),
						"lastName", is("ABC"),
						"userName", is("admin_abc"),
						"role", is("ADMIN"));

		assertThat(users.count()).isEqualTo(count);

		users.save(admin);
	}

	@Test
	@DisplayName("update user: without values ⇒ 💾 nothing + 🔙 validation errors")
	void updateUser_WithoutValues_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body((String) null)

				.when()
				.patch(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("update user: with blank values ⇒ 💾 nothing + 🔙 validation errors")
	void updateUser_WithBlankValues_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body(WITH_BLANK_VALUES)

				.when()
				.patch(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("update user: with too long values ⇒ 💾 nothing + 🔙 validation errors")
	void updateUser_WithTooLongValues_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body(WITH_TOO_LONG_VALUES)

				.when()
				.patch(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("update user: with forbidden symbols ⇒ 💾 nothing + 🔙 validation errors")
	void updateUser_WithForbiddenSymbols_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body(WITH_FORBIDDEN_SYMBOLS)

				.when()
				.patch(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());
		// .body(allOf(
		// containsString("firstName: Contains illegal characters"),
		// containsString("lastName: Contains illegal characters"),
		// containsString("userName: Contains illegal characters"),
		// containsString("password: Contains illegal characters")));

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("update user: with invalid password ⇒ 💾 nothing + 🔙 validation errors")
	void updateUser_WithInvalidPassword_ReturnsValidationErrors() {

		var count = users.count();

		given()
				.body(INVALID_PASSWORD)

				.when()
				.patch(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(BAD_REQUEST)
				// TODO Blank body must be fixed as a next step!
				.body(blankOrNullString());

		assertThat(users.count()).isEqualTo(count);
	}
}
