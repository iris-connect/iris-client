package iris.client_bff.auth.db;

import static io.restassured.http.ContentType.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static iris.client_bff.users.web.UsersTestData.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.WithMockAdmin;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import com.github.javafaker.Faker;

@IrisWebIntegrationTest
@WithMockAdmin
@RequiredArgsConstructor
@Tag("security")
@DisplayName("IT of the CSRF configuration")
class CsrfConfigIntegrationTest {

	private static final String BASE_URL = "/users";

	final MockMvc mvc;

	Faker faker = Faker.instance();

	@BeforeEach
	void init() {

		Locale.setDefault(Locale.ENGLISH);

		RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
				.setMockMvc(mvc)
				.setContentType(JSON)
				.build();
	}

	@AfterAll
	void cleanup() {

		// Must be done, otherwise other tests may break using RestAssured.
		RestAssuredMockMvc.requestSpecification = null;
	}

	@Test
	@DisplayName("create user: with CSRF Token ⇒ 🔙 201")
	void createUser_WithCsrfToken_ReturnsOk() throws Throwable {

		var username = faker.name().username();

		given()
				.auth().with(csrf())
				.body(VALID_USER.formatted(username))

				.when()
				.post(BASE_URL)

				.then()
				.status(CREATED);
	}

	@Test
	@DisplayName("create user: without CSRF Token ⇒ 🔙 403")
	void createUser_WithoutCsrfToken_ReturnsForbidden() throws Throwable {

		var username = faker.name().username();

		given()
				.body(VALID_USER.formatted(username))

				.when()
				.post(BASE_URL)

				.then()
				.status(FORBIDDEN);
	}

	@Test
	@DisplayName("create user: with wrong CSRF Token ⇒ 🔙 403")
	void createUser_WithWrongCsrfToken_ReturnsForbidden() throws Throwable {

		var username = faker.name().username();

		given()
				.auth().with(csrf().useInvalidToken())
				.body(VALID_USER.formatted(username))

				.when()
				.post(BASE_URL)

				.then()
				.status(FORBIDDEN);
	}
}
