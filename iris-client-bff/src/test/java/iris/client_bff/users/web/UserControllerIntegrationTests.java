package iris.client_bff.users.web;

import static io.restassured.http.ContentType.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static iris.client_bff.users.web.UsersTestData.*;
import static java.lang.Boolean.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.HttpStatus.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.WithMockAdmin;
import iris.client_bff.matchers.IsUuid;
import iris.client_bff.users.UserAccountsRepositoryForTests;
import iris.client_bff.users.web.dto.UserInsertDTO;
import iris.client_bff.users.web.dto.UserRoleDTO;
import iris.client_bff.users.web.dto.UserUpdateDTO;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

@IrisWebIntegrationTest
@WithMockAdmin
@RequiredArgsConstructor
@Tag("users")
@Tag("rest-controller")
@DisplayName("IT of web controller for users management")
class UserControllerIntegrationTests {

	private static final String BASE_URL = "/users";
	private static final String DETAILS_URL = BASE_URL + "/{id}";

	private static final FakeValuesService fake = new FakeValuesService(Locale.GERMAN, new RandomService());

	final MockMvc mvc;
	final ObjectMapper objectMapper;

	final UserAccountsRepositoryForTests users;

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
						"role", is("USER"),
						"locked", is(TRUE));

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
		var admin = users.findByUserName("admin");

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
						"role", is(admin.getRole().toString()),
						"locked", is(FALSE));

		assertThat(users.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("update user: valid JSON ⇒ 💾 changed user + 🔙 200 + changed user")
	void updateUser_ValidJson_ChangeUser() {

		var count = users.count();
		var admin = users.findByUserName("admin");

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
						"role", is("ADMIN"),
						"locked", is(TRUE));

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
	@DisplayName("update user: with blank values ⇒ 💾 nothing + 🔙 200")
	void updateUser_WithBlankValues_ReturnsValidationErrors() {

		var admin = users.findByUserName("admin");

		given()
				.body(WITH_BLANK_VALUES)

				.when()
				.patch(DETAILS_URL, admin.getId())

				.then()
				.status(OK);

		assertThat(users.findByUserName("admin")).usingRecursiveComparison().isEqualTo(admin);
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

	@ParameterizedTest(name = "{0} characters ⇒ expect = {1}")
	@CsvSource({ "0, BAD_REQUEST", "49, CREATED", "50,CREATED", "51, BAD_REQUEST", "1000, BAD_REQUEST" })
	@DisplayName("create user: field length validation for: userName")
	void createUser_userNameLength(int wordLength, HttpStatus expectation) throws Exception {

		var userName = createWord(wordLength);

		var dto = new UserInsertDTO("fn", "ln", userName, "Password12A_", UserRoleDTO.USER, false);

		given()
				.body(toJson(dto))

				.when()
				.post(BASE_URL)

				.then()
				.status(expectation);
	}

	@ParameterizedTest(name = "{0} characters ⇒ expect = {1}")
	@CsvSource({ "0, OK", "199, OK", "200,OK", "201, BAD_REQUEST", "1000, BAD_REQUEST" })
	@DisplayName("update user: field length validation for: lastName")
	void updateUser_lastNameLength(int wordLength, HttpStatus expectation) throws Exception {

		var admin = users.findByUserName("admin");
		var lastName = createWord(wordLength);
		var dto = UserUpdateDTO.builder().lastName(lastName).build();

		given()
				.body(toJson(dto))

				.when()
				.patch(DETAILS_URL, admin.getId())

				.then()
				.status(expectation);
	}

	private String createWord(int wordLength) {
		return fake.letterify("?".repeat(wordLength));
	}

	private String toJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}
}
