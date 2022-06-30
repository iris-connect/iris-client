package iris.client_bff.auth.db;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.MemoryAppender;
import iris.client_bff.auth.db.login_attempts.LoginAttemptsService;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class LoginAttemptsTests {

	private static final String baseUrl = "/login";

	private final MockMvc mvc;

	private MemoryAppender memoryAppender;

	@BeforeEach
	void setup() {

		Locale.setDefault(Locale.ENGLISH);

		RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
				.setMockMvc(mvc)
				.setContentType(ContentType.JSON)
				.build();

		Logger logger = (Logger) LoggerFactory.getLogger(LoginAttemptsService.class);
		memoryAppender = new MemoryAppender();
		memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.setLevel(Level.DEBUG);
		logger.addAppender(memoryAppender);
		memoryAppender.start();
	}

	@AfterAll
	void cleanup() {

		// Must be done, otherwise other tests may break using RestAssured.
		RestAssuredMockMvc.requestSpecification = null;
	}

	@Test
	void successfulLogin() throws Exception {

		given()
				.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

				.when().post(baseUrl)

				.then()
				.status(OK);
	}

	@Test
	void failedLogin() throws Exception {

		given()
				.body("{\"userName\":\"admin2\", \"password\":\"adminX\"}")

				.when().post(baseUrl)

				.then()
				.status(UNAUTHORIZED)
				.body("message", is("Bad credentials"));
	}

	@Test
	void blockedFailedLogins() throws Exception {

		var body = "{\"userName\":\"admin3\", \"password\":\"adminX\"}";

		given()
				.body(body)

				.when().post(baseUrl)

				.then()
				.status(UNAUTHORIZED)
				.body("message", is("Bad credentials"));

		given()
				.body(body)

				.when().post(baseUrl)

				.then()
				.status(UNAUTHORIZED)
				.body("message", startsWith("User blocked"));

		given()
				.body(body)

				.when().post(baseUrl)

				.then()
				.status(UNAUTHORIZED)
				.body("message", startsWith("User blocked"));
	}

	@Test
	void loggingAfterThreeFailedLogins() throws Exception {

		var body = "{\"userName\":\"admin4\", \"password\":\"adminX\"}";

		given()
				.body(body)

				.when().post(baseUrl)

				.then()
				.status(UNAUTHORIZED)
				.body("message", is("Bad credentials"));

		Thread.sleep(4000);

		given()
				.body(body)

				.when().post(baseUrl)

				.then()
				.status(UNAUTHORIZED)
				.body("message", is("Bad credentials"));

		Thread.sleep(7000);

		given()
				.body(body)

				.when().post(baseUrl)

				.then()
				.status(UNAUTHORIZED)
				.body("message", is("Bad credentials"));

		var nameHash = DigestUtils.md5Hex("admin4");

		assertThat(memoryAppender.countEventsForLogger(LoginAttemptsService.class)).isEqualTo(1);
		assertThat(memoryAppender.contains(
				"For user name (hash: " + nameHash
						+ ") there were 3 failed attempts to log in to the IRIS client. The next warning occurs at 30.",
				Level.WARN)).isTrue();
	}
}
