package iris.client_bff.auth.db;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.MemoryAppender;
import iris.client_bff.auth.db.login_attempts.LoginAttemptsService;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
public class LoginAttemptsTests {

	private static final String baseUrl = "/login";

	@Autowired
	private MockMvc mvc;

	private MemoryAppender memoryAppender;

	@BeforeEach
	public void setup() {
		Logger logger = (Logger) LoggerFactory.getLogger(LoginAttemptsService.class);
		memoryAppender = new MemoryAppender();
		memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.setLevel(Level.DEBUG);
		logger.addAppender(memoryAppender);
		memoryAppender.start();
	}

	@Test
	void successfulLogin() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post(baseUrl)
				.content("{\"userName\":\"admin\", \"password\":\"admin\"}"))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void failedLogin() throws Exception {

		given().mockMvc(mvc)
				.body("{\"userName\":\"admin2\", \"password\":\"adminX\"}")
				.when().post(baseUrl)
				.then()
				.statusCode(401)
				.assertThat(errorContains("Unauthorized"));
	}

	@Test
	void blockedFailedLogins() throws Exception {

		var body = "{\"userName\":\"admin3\", \"password\":\"adminX\"}";

		given().mockMvc(mvc)
				.body(body)
				.when().post(baseUrl)
				.then()
				.statusCode(401)
				.assertThat(errorContains("Unauthorized"));

		given().mockMvc(mvc)
				.body(body)
				.when().post(baseUrl)
				.then()
				.statusCode(401)
				.assertThat(errorContains("User blocked"));

		given().mockMvc(mvc)
				.body(body)
				.when().post(baseUrl)
				.then()
				.statusCode(401)
				.assertThat(errorContains("User blocked"));
	}

	@Test
	void loggingAfterThreeFailedLogins() throws Exception {

		var body = "{\"userName\":\"admin4\", \"password\":\"adminX\"}";

		given().mockMvc(mvc)
				.body(body)
				.when().post(baseUrl)
				.then()
				.statusCode(401)
				.assertThat(errorContains("Unauthorized"));

		Thread.sleep(4000);

		given().mockMvc(mvc)
				.body(body)
				.when().post(baseUrl)
				.then()
				.statusCode(401)
				.assertThat(errorContains("Unauthorized"));

		Thread.sleep(7000);

		given().mockMvc(mvc)
				.body(body)
				.when().post(baseUrl)
				.then()
				.statusCode(401)
				.assertThat(errorContains("Unauthorized"));

		var nameHash = DigestUtils.md5Hex("admin4");

		assertThat(memoryAppender.countEventsForLogger(LoginAttemptsService.class)).isEqualTo(1);
		assertThat(memoryAppender.contains(
				"For user name (hash: " + nameHash
						+ ") there were 3 failed attempts to log in to the IRIS client. The next warning occurs at 30.",
				Level.WARN)).isTrue();
	}

	ResultMatcher errorContains(final String message) {
		return result -> assertThat(result.getResponse().getErrorMessage()).contains(message);
	}
}
