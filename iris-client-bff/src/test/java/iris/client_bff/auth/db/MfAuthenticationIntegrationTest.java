package iris.client_bff.auth.db;

import static io.restassured.http.ContentType.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static java.time.Instant.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.HttpStatus.*;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import io.restassured.http.Cookie;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.WithMockAdmin;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@IrisWebIntegrationTest
@SpringBootTest(properties = { "security.auth.db.mfa.option=OPTIONAL_DEFAULT_FALSE" })
@RequiredArgsConstructor
@Tag("security")
@DisplayName("IT of the MFA implementation")
class MfAuthenticationIntegrationTest {

	private static final String BASE_URL = "/login";
	private static final String MFA_URL = "/mfa/otp";
	private static final String IRIS_COOKIE = "IRIS_JWT";
	private static final String REFRESH_COOKIE = "IRIS_REFRESH_JWT";

	final MockMvc mvc;
	final UserService userService;
	final CodeGenerator codeGenerator;

	@BeforeEach
	void init() {

		Locale.setDefault(Locale.ENGLISH);

		RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
				.setMockMvc(mvc)
				.setContentType(JSON)
				.build();
	}

	@AfterEach
	void resetUser() {

		var user = userService.findByUsername("admin").get();

		// Must be set after the login attempt to allow the subsequent change of data.
		SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, null, "ADMIN"));
		userService.updateUseMfa(user, false);
	}

	@AfterAll
	void cleanup() {

		// Must be done, otherwise other tests may break using RestAssured.
		RestAssuredMockMvc.requestSpecification = null;
	}

	@Test
	@DisplayName("login: valid input + without MFA")
	void login_WithoutMfa() throws Throwable {
		given()
				.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

				.when()
				.post(BASE_URL)

				.then()
				.status(OK)
				.body("authenticationStatus", equalTo("AUTHENTICATED"),
						"qrCodeImageUri", blankOrNullString(),
						"mfaSecret", blankOrNullString())
				.cookie(IRIS_COOKIE)
				.cookie(REFRESH_COOKIE);
	}

	@Test
	@WithMockAdmin
	@DisplayName("MFA: without enrolled secret")
	void login_WithMfa_WithoutEnrolledSecret() throws Throwable {

		var user = userService.findByUsername("admin").get();
		userService.updateUseMfa(user, true);

		// the login request
		var responce = given()
				.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

				.when()
				.post(BASE_URL)

				.then()
				.status(OK)
				.body("authenticationStatus", equalTo("PRE_AUTHENTICATED_ENROLLMENT_REQUIRED"),
						"qrCodeImageUri", startsWith("data:image/png;base64,"),
						"mfaSecret", not(blankOrNullString()))
				.cookie(IRIS_COOKIE, detailedCookie().expiryDate(lessThan(Date.from(now().plusSeconds(5 * 60)))))
				.extract();

		var jwtCookie = responce.detailedCookie(IRIS_COOKIE);
		String secret = responce.path("mfaSecret");

		// request to /user-profile is forbidden with this token
		requestUserProfileIsForbidden(jwtCookie);

		// a code verification with a wrong OTP
		verifyWrongOtp(jwtCookie);

		// a code verification with correct OTP
		jwtCookie = verifyCorrectOtp(secret, jwtCookie);

		// request to /user-profile is ok after full MFA
		requestUserProfileIsOk(jwtCookie);

		assertThat(userService.findByUsername("admin").get().isMfaSecretEnrolled()).isTrue();
	}

	@Test
	@WithMockAdmin
	@DisplayName("MFA: with enrolled secret")
	void login_WithMfa_WithEnrolledSecret() throws Throwable {

		var user = userService.findByUsername("admin").get();
		userService.updateUseMfa(user, true);
		var verify = userService.finishEnrollment(user, createOtp(user.getMfaSecret()));

		assertThat(verify).isTrue();
		assertThat(user.isMfaSecretEnrolled()).isTrue();

		// the login request
		var jwtCookie = given()
				.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

				.when()
				.post(BASE_URL)

				.then()
				.status(OK)
				.body("authenticationStatus", equalTo("PRE_AUTHENTICATED_MFA_REQUIRED"),
						"qrCodeImageUri", blankOrNullString(),
						"mfaSecret", blankOrNullString())
				.cookie(IRIS_COOKIE, detailedCookie().expiryDate(lessThan(Date.from(now().plusSeconds(5 * 60)))))
				.extract()
				.detailedCookie(IRIS_COOKIE);

		// request to /user-profile is forbidden with this token
		requestUserProfileIsForbidden(jwtCookie);

		// a code verification with a wrong OTP
		verifyWrongOtp(jwtCookie);

		// a code verification with correct OTP
		jwtCookie = verifyCorrectOtp(user.getMfaSecret(), jwtCookie);

		// request to /user-profile is ok after full MFA
		requestUserProfileIsOk(jwtCookie);
	}

	private String createOtp(String secret) throws CodeGenerationException {
		return codeGenerator.generate(secret, Math.floorDiv(now().getEpochSecond(), 30));
	}

	private void requestUserProfileIsForbidden(Cookie jwtCookie) {

		given()
				.cookie(jwtCookie)

				.when()
				.get("/user-profile")

				.then()
				.status(FORBIDDEN);
	}

	private void verifyWrongOtp(Cookie jwtCookie) {

		given()
				.cookie(jwtCookie)
				.body("{\"otp\":\"000000\"}")

				.when()
				.post(MFA_URL)

				.then()
				.status(UNAUTHORIZED)
				.body("message", equalTo("Invalid verification code"));
	}

	private Cookie verifyCorrectOtp(String secret, Cookie jwtCookie) throws CodeGenerationException {

		return given()
				.cookie(jwtCookie)
				.body(String.format("{\"otp\":\"%s\"}", createOtp(secret)))

				.when()
				.post(MFA_URL)

				.then()
				.status(OK)
				.body("authenticationStatus", equalTo("AUTHENTICATED"),
						"qrCodeImageUri", blankOrNullString(),
						"mfaSecret", blankOrNullString())
				.cookie(IRIS_COOKIE)
				.cookie(REFRESH_COOKIE)
				.extract()
				.detailedCookie(IRIS_COOKIE);
	}

	private void requestUserProfileIsOk(Cookie jwtCookie) {
		given()
				.cookie(jwtCookie)

				.when()
				.get("/user-profile")

				.then()
				.status(OK);
	}

	@IrisWebIntegrationTest
	@SpringBootTest(properties = { "security.auth.db.mfa.option=always" })
	@RequiredArgsConstructor
	@Tag("security")
	@DisplayName("IT of the MFA implementation with mfa.option=always")
	static class MfaAlways {

		final MockMvc mvc;

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
		@DisplayName("login: invalid input ⇒ 🔙 401")
		void login_InvalidInput_Returns401() throws Throwable {

			given()
					.body("{\"userName\":\"a\", \"password\":\"a\"}")

					.when()
					.post(BASE_URL)

					.then()
					.status(UNAUTHORIZED);
		}

		@Test
		@DisplayName("login: valid input + without enrolled secret ⇒ 🔙 200 + PRE_AUTHENTICATED_ENROLLMENT_REQUIRED")
		void login_ValidInput_ReturnsEnrollmentRequired() throws Throwable {
			given()
					.body("{\"userName\":\"admin\", \"password\":\"admin\"}")

					.when()
					.post(BASE_URL)

					.then()
					.status(OK)
					.body("authenticationStatus", equalTo("PRE_AUTHENTICATED_ENROLLMENT_REQUIRED"),
							"qrCodeImageUri", startsWith("data:image/png;base64,"),
							"mfaSecret", not(blankOrNullString()))
					.cookie(IRIS_COOKIE, detailedCookie().expiryDate(lessThan(Date.from(now().plusSeconds(5 * 60)))));
		}
	}
}
