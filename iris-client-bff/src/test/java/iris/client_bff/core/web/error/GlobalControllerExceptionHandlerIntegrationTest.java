package iris.client_bff.core.web.error;

import static io.restassured.http.ContentType.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.WithMockAdmin;
import iris.client_bff.core.web.error.TestController.TestException;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

@IrisWebIntegrationTest
@WithMockAdmin
@RequiredArgsConstructor
@DisplayName("IT for global exception handler → checks clean error responses")
class GlobalControllerExceptionHandlerIntegrationTest {

	static final String PATH = "/test_exception_hander";

	final MockMvc mvc;

	@BeforeEach
	void init() {

		Locale.setDefault(Locale.GERMAN);

		RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
				.setMockMvc(mvc)
				.setContentType(JSON)
				.addHeader("Accept-Language", "de")
				.setBasePath(PATH).build();
	}

	@AfterAll
	void cleanup() {

		// Must be done, otherwise other tests may break using RestAssured.
		RestAssuredMockMvc.requestSpecification = null;
	}

	@Test
	@DisplayName("Cross check → call works")
	void callWorks() throws Throwable {

		given()
				.body("{\"text\":\"ab\"}")

				.when()
				.post()

				.then()
				.status(OK);
	}

	@Test
	@DisplayName("Test with MethodArgumentNotValidException")
	void methodArgumentNotValidException() throws Throwable {

		var status = BAD_REQUEST;

		given()
				.body("{\"text\":\"a\", \"text2\":\"a\"}")

				.when()
				.post()

				.then()
				.log().all()
				.status(status)
				.body("status", is(status.value()),
						"error", is(status.getReasonPhrase()),
						"message", startsWith("Bitte prüfen sie ihre Eingabe: [text2 ⇒ Größe muss zwischen 2 und 2147483647 sein;"),
						"errors.text[0]", is("Größe muss zwischen 2 und 2147483647 sein"),
						"timestamp", not(emptyOrNullString()),
						"trace", containsString(MethodArgumentNotValidException.class.getSimpleName()));
	}

	@Test
	@DisplayName("Test with ConstraintViolationException for body")
	void constraintViolationException_body() throws Throwable {

		var status = BAD_REQUEST;

		given()
				.body("{\"text\":\"a\", \"text2\":\"a\"}")

				.when()
				.put()

				.then()
				.log().all()
				.status(status)
				.body("status", is(status.value()),
						"error", is(status.getReasonPhrase()),
						"message", startsWith("Bitte prüfen sie ihre Eingabe: [text2 ⇒ Größe muss zwischen 2 und 2147483647 sein;"),
						"errors.text[0]", is("Größe muss zwischen 2 und 2147483647 sein"),
						"timestamp", not(emptyOrNullString()),
						"trace", containsString(ConstraintViolationException.class.getSimpleName()));
	}

	@Test
	@DisplayName("Test with ConstraintViolationException for param")
	void constraintViolationException_param() throws Throwable {

		var status = BAD_REQUEST;

		given()
				.param("param", "a")

				.when()
				.get()

				.then()
				.log().all()
				.status(status)
				.body("status", is(status.value()),
						"error", is(status.getReasonPhrase()),
						"message",
						is("Bitte prüfen sie ihre Eingabe: [withoutBody.param ⇒ Größe muss zwischen 2 und 2147483647 sein]"),
						"errors.'withoutBody.param'[0]", is("Größe muss zwischen 2 und 2147483647 sein"),
						"timestamp", not(emptyOrNullString()),
						"trace", containsString(ConstraintViolationException.class.getSimpleName()));
	}

	@Test
	@DisplayName("Test with HttpMessageNotReadableException")
	void httpMessageNotReadableException() throws Throwable {

		var status = BAD_REQUEST;

		given()
				.body((String) null)

				.when()
				.post()

				.then()
				.status(status)
				.body("status", is(status.value()),
						"error", is(status.getReasonPhrase()),
						"message", containsString("Required request body is missing"),
						"timestamp", not(emptyOrNullString()),
						"trace", containsString(HttpMessageNotReadableException.class.getSimpleName()));
	}

	@Test
	@DisplayName("Test with Exception with @ResponseStatus")
	void responseStatus() throws Throwable {

		when()
				.delete()

				.then()
				.log().all()
				.status(I_AM_A_TEAPOT);
	}

	@Test
	@DisplayName("Test with unhandled Exception")
	void unhandledException() throws Throwable {

		var status = INTERNAL_SERVER_ERROR;

		when()
				.delete("/a")

				.then()
				.log().all()
				.status(status)
				.body("status", is(status.value()),
						"error", is(status.getReasonPhrase()),
						"message", is("Test"),
						"timestamp", not(emptyOrNullString()),
						"trace", containsString(TestException.class.getSimpleName()));
	}

	@Test
	@DisplayName("Test with IRISSearchException")
	void iRISSearchException() throws Throwable {

		when()
				.delete("/IRISSearchException")

				.then()
				.log().all()
				.status(SERVICE_UNAVAILABLE);
	}

	@Test
	@DisplayName("Test with IRISDataRequestException")
	void iRISDataRequestException() throws Throwable {

		when()
				.delete("/IRISDataRequestException")

				.then()
				.log().all()
				.status(INTERNAL_SERVER_ERROR);
	}

	@Test
	@DisplayName("Test with BlockLimitExceededException")
	void blockLimitExceededException() throws Throwable {

		when()
				.delete("/BlockLimitExceededException")

				.then()
				.log().all()
				.status(PAYLOAD_TOO_LARGE);
	}

	@Test
	@DisplayName("Test with ResponseStatusException")
	void responseStatusException() throws Throwable {

		when()
				.delete("/ResponseStatusException")

				.then()
				.log().all()
				.status(I_AM_A_TEAPOT);
	}

	@Test
	@DisplayName("Test login with empty body")
	void login_WithoutBody() throws Throwable {

		setSpecForLogin();

		when()
				.post()

				.then()
				.log().all()
				.status(BAD_REQUEST)
				.body("message", containsString("Required request body is missing"));
	}

	@Test
	@DisplayName("Test login with wrong userName")
	void login_WrongUserName() throws Throwable {

		// sleep to avoid blocking
		Thread.sleep(2000);

		var status = UNAUTHORIZED;

		setSpecForLogin();

		given()
				.body("{\"userName\":\"a\",\"password\":\"admin\"}")

				.when()
				.post()

				.then()
				.log().all()
				.status(status)
				.body("message", containsString("Ungültige Anmeldedaten"));
	}

	@Test
	@DisplayName("Test login with wrong password")
	void login_WrongPassword() throws Throwable {

		// sleep to avoid blocking
		Thread.sleep(2000);

		var status = UNAUTHORIZED;

		setSpecForLogin();

		given()
				.body("{\"userName\":\"admin\",\"password\":\"a\"}")

				.when()
				.post()

				.then()
				.log().all()
				.status(status)
				.body("message", containsString("Ungültige Anmeldedaten"));
	}

	@Test
	@DisplayName("Test login with blocked user")
	void login_BlockedUser() throws Throwable {

		// sleep to avoid blocking
		Thread.sleep(2000);

		var status = UNAUTHORIZED;

		setSpecForLogin();

		given().body("{\"userName\":\"admin\",\"password\":\"a\"}").when().post();

		given()
				.body("{\"userName\":\"admin\",\"password\":\"a\"}")

				.when()
				.post()

				.then()
				.log().all()
				.status(status)
				.body("message", containsString("User blocked!"));
	}

	@Test
	@WithMockUser
	@DisplayName("Test forbidden access")
	void getAllUsers_ForbiddenAccess() {

		setSpecForUsers();

		when()
				.get()

				.then()
				.log().all()
				.status(FORBIDDEN)
				.body("message", containsString("Zugriff verweigert"));
	}

	private void setSpecForLogin() {
		RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
				.setMockMvc(mvc)
				.setContentType(JSON)
				.setBasePath("/login").build();
	}

	private void setSpecForUsers() {
		RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
				.setMockMvc(mvc)
				.setContentType(JSON)
				.setBasePath("/users").build();
	}
}
