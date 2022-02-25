package iris.client_bff.vaccination_info.eps;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static iris.client_bff.matchers.IrisMatchers.*;
import static iris.client_bff.vaccination_info.eps.VaccinationInfoAnnouncmentTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.HamcrestCondition.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.proxy.EPSProxyServiceServiceClient;
import iris.client_bff.vaccination_info.EncryptionService;
import iris.client_bff.vaccination_info.VaccinationInfoRepository;
import lombok.RequiredArgsConstructor;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
@Tag("vaccination-proof")
@DisplayName("IT of JSON-RPC controller for vaccination info")
class VaccinationInfoControllerIntegrationTest {

	static final String JSON_RPC = "application/json-rpc";

	final MockMvc mvc;
	final VaccinationInfoRepository vacInfos;
	final EncryptionService encryptionService;

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
	@DisplayName("announce vaccination info list: valid JSON ⇒ 💾 vaccination info + 🔙 encrypted tokens")
	void announceVaccinationInfoList_ValidJson_ReturnsEncryptedTokens() throws Throwable {

		assertThat(vacInfos.count()).isZero();

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

		assertThat(vacInfos.count()).isNotZero();

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

	@ParameterizedTest(name = "{1}")
	@MethodSource(value = "getJsonInput")
	@DisplayName("announce vaccination info list: invalid JSON ⇒ 💾 nothing + 🔙 validation errors")
	void announceVaccinationInfoList_InvalidJson_ReturnsValidationErrors(String json, String hint, String errorMessage) {

		var savedInfos = vacInfos.count();

		given()
				.body(json)

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(BAD_REQUEST)
				.body("error.data.message", allOf(
						containsString("externalId: " + errorMessage),
						containsString("submitterPublicKey: " + errorMessage)));

		assertThat(vacInfos.count()).isEqualTo(savedInfos);
	}

	String[][] getJsonInput() {

		return new String[][] { { ANNOUNCEMENT_WITHOUT_VALUES, "Without attributes", "must not be blank" },
				{ ANNOUNCEMENT_WITH_EMPTY_VALUES, "With empty values", "must not be blank" },
				{ ANNOUNCEMENT_WITH_FORBIDDEN_VALUES, "With forbidden characters in values",
						"Contains illegal characters" } };
	}
}
