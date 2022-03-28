package iris.client_bff.vaccination_info.eps;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static iris.client_bff.matchers.IrisMatchers.*;
import static iris.client_bff.vaccination_info.eps.VaccinationInfoAnnouncmentTestData.*;
import static iris.client_bff.vaccination_info.eps.VaccinationInfoSubmissionTestData.*;
import static java.lang.String.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.HamcrestCondition.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.proxy.EPSProxyServiceServiceClient;
import iris.client_bff.vaccination_info.EncryptionService;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncementRepository;
import iris.client_bff.vaccination_info.VaccinationInfoRepository;
import lombok.RequiredArgsConstructor;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
@Tag("vaccination-info")
@Tag("json-rpc-controller")
@DisplayName("IT of JSON-RPC controller for vaccination info")
class VaccinationInfoControllerImplIntegrationTests {

	static final String JSON_RPC = "application/json-rpc";

	final MockMvc mvc;
	final VaccinationInfoAnnouncementRepository announcements;
	final EncryptionService encryptionService;
	final VaccinationInfoRepository vaccInfos;

	@MockBean
	final AlertService alerts;

	@SpyBean
	final EPSProxyServiceServiceClient proxyClient;

	@Captor
	ArgumentCaptor<Instant> announcementCaptor;

	@BeforeEach
	void init() {

		Locale.setDefault(Locale.ENGLISH);

		RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
				.setMockMvc(mvc)
				.setContentType(JSON_RPC).build();

		RestAssuredMockMvc.responseSpecification = new ResponseSpecBuilder()
				.expectContentType(JSON_RPC)
				.registerParser(JSON_RPC, Parser.JSON)
				.expectBody("id", equalTo("1"))
				.build();
	}

	@AfterAll
	void cleanup() {

		// Must be done, otherwise other tests may break using RestAssured.
		RestAssuredMockMvc.requestSpecification = null;
		RestAssuredMockMvc.responseSpecification = null;
	}

	@Test
	@DisplayName("announce vaccination info list: valid JSON ⇒ 💾 announcement + 🔙 encrypted tokens")
	void announceVaccinationInfoList_ValidJson_ReturnsEncryptedTokens() throws Throwable {

		var count = announcements.count();

		var keyPair = encryptionService.generateKeyPair();
		var pubKeyBase64 = encryptionService.encodeToBase64(keyPair.getPublic());

		var response = given()
				.body(String.format(VALID_ANNOUNCEMENT, pubKeyBase64))

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(OK)
				.extract()
				.jsonPath();

		var hdKeyBase64 = response.getString("result.hdPublicKey");
		var ivBase64 = response.getString("result.iv");
		var tokensBase64 = response.getString("result.tokens");

		assertThat(hdKeyBase64).isBase64().isNotBlank();
		assertThat(ivBase64).isBase64().isNotBlank();
		assertThat(tokensBase64).isBase64().isNotBlank();

		var tokenJson = decrypt(keyPair.getPrivate(), hdKeyBase64, ivBase64, tokensBase64);

		var tokens = JsonPath.from(tokenJson);

		assertThat(tokens.getString("cat")).is(matching(isCatWith(".proxy.dev.test-gesundheitsamt.de")));
		assertThat(tokens.getString("dat")).is(matching(isUuid()));

		assertThat(announcements.count()).isEqualTo(count + 1);

		verify(proxyClient).announce(announcementCaptor.capture());

		var inTwoHours = Instant.now().plus(Duration.ofHours(2));
		assertThat(announcementCaptor.getValue()).isBeforeOrEqualTo(inTwoHours);
	}

	private String decrypt(PrivateKey privateKey, String hdKeyBase64, String ivBase64, String tokensBase64)
			throws GeneralSecurityException {

		var hdKey = encryptionService.decodeFromBase64(hdKeyBase64);
		var aesKey = encryptionService.generateAgreedKey(privateKey, hdKey);
		var tokenJson = encryptionService.decodeAndDecrypt(aesKey, ivBase64, tokensBase64);

		return tokenJson;
	}

	@Test
	@DisplayName("announce vaccination info list: invalid Base64 of public key ⇒ 💾 nothing + 🔙 validation error")
	void announceVaccinationInfoList_InvalidBase64PublicKey_ReturnsValidationErrors() {

		var count = announcements.count();

		given()
				.body(String.format(VALID_ANNOUNCEMENT, "x x"))

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.data.message", containsString("submitterPublicKey: No valid Base64 encoding"));

		assertThat(announcements.count()).isEqualTo(count);
	}

	@Test
	@DisplayName("announce vaccination info list: invalid public key ⇒ 💾 nothing + 🔙 500")
	void announceVaccinationInfoList_InvalidPublicKey_Returns500() {

		var count = announcements.count();

		given()
				.body(String.format(VALID_ANNOUNCEMENT, "XXX"))

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.message",
						containsString("submitterPublicKey: The passed public key contains errors and cannot be used"));

		assertThat(announcements.count()).isEqualTo(count);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource(value = "getAnnouncementJsonInput")
	@DisplayName("announce vaccination info list: invalid JSON ⇒ 💾 nothing + 🔙 validation errors")
	void announceVaccinationInfoList_InvalidJson_ReturnsValidationErrors(String json, String hint, String errorMessage) {

		var count = announcements.count();

		given()
				.body(json)

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.data.message", allOf(
						containsString("externalId: " + errorMessage),
						containsString("submitterPublicKey: " + errorMessage)));

		assertThat(announcements.count()).isEqualTo(count);
	}

	String[][] getAnnouncementJsonInput() {

		return new String[][] { { ANNOUNCEMENT_WITHOUT_VALUES, "Without attributes", "must not be blank" },
				{ ANNOUNCEMENT_WITH_EMPTY_VALUES, "With empty values", "must not be blank" },
				{ ANNOUNCEMENT_WITH_FORBIDDEN_VALUES, "With forbidden characters in values",
						"Contains illegal characters" } };
	}

	@Test
	@DisplayName("submit vaccination info list: valid JSON ⇒ 💾 vaccination info + 🔙 \"OK\"")
	void submitVaccinationInfoList_ValidJson_ReturnsOk() throws Throwable {

		var count = vaccInfos.count();
		var announcement = announcements.findAll().get(0);

		given()
				.body(format(VALID_SUBMISSION, announcement.getId().toString()))

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(OK)
				.body("result", is("OK"));

		assertThat(vaccInfos.count()).isEqualTo(count + 1);
	}

	@Test
	@DisplayName("submit vaccination info list: unknown DAT ⇒ 💾 nothing + 🔙 \"OK\"")
	void submitVaccinationInfoList_UnknownDat_Returns500() throws Throwable {

		var count = vaccInfos.count();

		given()
				.body(format(VALID_SUBMISSION, UUID.randomUUID()))

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.message", containsString("Unknown dataAuthorizationToken:"));

		assertThat(vaccInfos.count()).isEqualTo(count);

		verify(alerts).createAlertMessage(anyString(), anyString());
		verifyNoMoreInteractions(alerts);
	}

	@Test
	@DisplayName("submit vaccination info list: JSON without params ⇒ 💾 nothing + 🔙 \"500 - method parameters invalid\"")
	void submitVaccinationInfoList_WithoutParams_Returns500() {

		var savedInfos = announcements.count();

		given()
				.body(SUBMISSION_WITHOUT_PARAMS)

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.data.message", containsString("employees: must not be empty"));

		assertThat(announcements.count()).isEqualTo(savedInfos);
	}

	@Test
	@DisplayName("submit vaccination info list: JSON without values ⇒ 💾 nothing + 🔙 validation errors")
	void submitVaccinationInfoList_WithoutValues_ReturnsValidationErrors() {

		var savedInfos = announcements.count();

		given()
				.body(SUBMISSION_WITHOUT_VALUES)

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.data.message", allOf(
						containsString("dataAuthorizationToken: must not be null"),
						containsString("name: must not be blank"),
						containsString("address: must not be null"),
						containsString("contactPerson: must not be null"),
						containsString("employees: must not be empty")));

		assertThat(announcements.count()).isEqualTo(savedInfos);
	}

	@Test
	@DisplayName("submit vaccination info list: invalid mail + phone ⇒ 💾 nothing + 🔙 validation errors")
	void submitVaccinationInfoList_InvalidMailPhone_ReturnsValidationErrors() {

		var savedInfos = announcements.count();

		given()
				.body(SUBMISSION_INVALID_MAIL_PHONE)

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.data.message", allOf(
						containsString("contactPerson.eMail: must be a well-formed email address"),
						containsString("contactPerson.phone: Invalid phone number")));

		assertThat(announcements.count()).isEqualTo(savedInfos);
	}

	@Test
	@DisplayName("submit vaccination info list: invalid employee ⇒ 💾 nothing + 🔙 validation errors")
	void submitVaccinationInfoList_InvalidEmployee_ReturnsValidationErrors() {

		var savedInfos = announcements.count();

		given()
				.body(SUBMISSION_INVALID_EMPLOYEES)

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.data.message", allOf(
						containsString("employees[].firstName: must not be blank"),
						containsString("employees[].lastName: must not be blank"),
						containsString("employees[].dateOfBirth: must not be null"),
						containsString("employees[].sex: must not be null"),
						containsString("employees[].vaccination: must not be null"),
						containsString("employees[].vaccinationStatus: must not be null"),
						containsString("employees[].address: must not be null")));

		assertThat(announcements.count()).isEqualTo(savedInfos);
	}

	@Test
	@DisplayName("announce vaccination info list: with forbidden symbols ⇒ 💾 nothing + 🔙 validation errors")
	void announceVaccinationInfoList_WithForbiddenSymbols_ReturnsValidationErrors() {

		var count = announcements.count();

		given()
				.body(SUBMISSION_WITH_FORBIDDEN_SYMBOLS)

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.data.message", allOf(
						containsString("contactPerson.eMail: must be a well-formed email address"),
						containsString("employees[].phone: Invalid phone number"),

						containsString("facility.name: Contains illegal characters"),
						containsString("address.street: Contains illegal characters"),
						containsString("address.houseNumber: Contains illegal characters"),
						containsString("address.zipCode: Contains illegal characters"),
						containsString("address.city: Contains illegal characters"),
						containsString("contactPerson.firstName: Contains illegal characters"),
						containsString("contactPerson.lastName: Contains illegal characters"),
						containsString("contactPerson.phone: Contains illegal characters"),
						containsString("contactPerson.eMail: Contains illegal characters"),
						containsString("employees[].firstName: Contains illegal characters"),
						containsString("employees[].lastName: Contains illegal characters"),
						containsString("employees[].address.street: Contains illegal characters"),
						containsString("employees[].address.houseNumber: Contains illegal characters"),
						containsString("employees[].address.zipCode: Contains illegal characters"),
						containsString("employees[].address.city: Contains illegal characters"),
						containsString("employees[].phone: Contains illegal characters"),
						containsString("employees[].eMail: Contains illegal characters")));

		assertThat(announcements.count()).isEqualTo(count);
	}
}
