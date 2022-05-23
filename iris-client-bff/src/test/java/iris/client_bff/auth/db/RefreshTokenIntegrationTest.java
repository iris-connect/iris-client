package iris.client_bff.auth.db;

import static io.restassured.http.ContentType.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@IrisWebIntegrationTest
@SpringBootTest(properties = { "security.jwt.expiration-time=1s" })
@WithMockAdmin
@RequiredArgsConstructor
@Tag("security")
@DisplayName("IT of the Refresh JWT implementation")
class RefreshTokenIntegrationTest {

	private static final String BASE_URL = "/login";
	private static final String REFRESH_URL = "/refreshtoken";
	private static final String IRIS_COOKIE = "IRIS_JWT";
	private static final String REFRESH_COOKIE = "IRIS_REFRESH_JWT";

	final MockMvc mvc;

	// Faker faker = Faker.instance();

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
	@DisplayName("login: with valid input ⇒ 🔙 200 + 2. cookies")
	void login_ValidInput_ReturnsCookies() throws Throwable {

		given()
				.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

				.when()
				.post(BASE_URL)

				.then()
				.status(OK)
				.cookie(IRIS_COOKIE)
				.cookie(REFRESH_COOKIE);
	}

	@Test
	@DisplayName("refresh token: with not expired token ⇒ 🔙 400")
	void refreshtoken_TokenNotExpired_BadRequest() throws Throwable {

		var jwtCookie = given()
				.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

				.when()
				.post(BASE_URL)

				.then()
				.status(OK)
				.extract()
				.detailedCookie(IRIS_COOKIE);

		when()
				.get(REFRESH_URL)

				.then()
				.status(BAD_REQUEST)
				.statusLine(containsString("There isn't a usable JWT, but this is required for refresh the token."));

		given()
				.cookie(jwtCookie)

				.when()
				.get(REFRESH_URL)

				.then()
				.status(BAD_REQUEST)
				.statusLine(containsString("The JWT isn't expired and you can't refresh a not expired token."));
	}

	@Test
	@DisplayName("refresh token: without refresh cookie ⇒ 🔙 400")
	void refreshtoken_MissingRefreshCookie_BadRequest() throws Throwable {

		var jwtCookie = given()
				.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

				.when()
				.post(BASE_URL)

				.then()
				.status(OK)
				.extract()
				.detailedCookie(IRIS_COOKIE);

		Thread.sleep(1100);

		given()
				.cookie(jwtCookie)

				.when()
				.get(REFRESH_URL)

				.then()
				.status(BAD_REQUEST)
				.statusLine(containsString("A usable Refresh JWT is missing!"));
	}

	@Test
	@DisplayName("refresh token: with valid input ⇒ 🔙 200 + new JWT")
	void refreshtoken_ValidInput_NewJwt() throws Throwable {

		var cookies = given()
				.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

				.when()
				.post(BASE_URL)

				.then()
				.status(OK)
				.extract()
				.detailedCookies();

		Thread.sleep(1100);

		given()
				.cookies(cookies)

				.when()
				.get(REFRESH_URL)

				.then()
				.status(NO_CONTENT)
				.cookie(IRIS_COOKIE, not(cookies.get(IRIS_COOKIE)));
	}

	// @Test
	@DisplayName("create user: without CSRF Token ⇒ 🔙 403")
	void createUser_WithoutCsrfToken_ReturnsForbidden() throws Throwable {

	}

	// @Test
	@DisplayName("create user: with wrong CSRF Token ⇒ 🔙 403")
	void createUser_WithWrongCsrfToken_ReturnsForbidden() throws Throwable {

	}
}
