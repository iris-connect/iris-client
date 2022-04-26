package iris.client_bff.config;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@Tag("jackson")
@DisplayName("JSON ⇔ DTO mapping ⇒ Jackson config")
@RequiredArgsConstructor
class JacksonIntegrationTests {

	private final ObjectMapper om;

	@Test
	@DisplayName("JSON: Empty string ⇒ DTO: Null ")
	void mappingFromJson_BlankString_ToNull() throws Exception {
		assertThat(om.readValue("{ \"value\" : \"\" }", Sample.class).value).isNull();
		assertThat(om.readValue("{ \"value\" : \" \" }", Sample.class).value).isNull();
	}

	@Test
	@DisplayName("JSON: String with surrounding spaces ⇒ DTO: Trimmed")
	void mappingFromJson_StringWithSpaces_ToTrimmedValue() throws Exception {
		assertThat(om.readValue("{ \"value\" : \" foo bar \" }", Sample.class).value).isEqualTo("foo bar");
	}

	@Test
	@DisplayName("DTO: Null ⇒ JSON: Not included")
	void mappingFromObject_NullValue_NotMapped() throws Exception {
		assertThat(om.writeValueAsString(new Sample(null))).isEqualTo("{ }");
	}

	@Test
	@DisplayName("DTO: An object inherits from IdWithUuid ⇒ JSON: String of the UUID without object")
	void mappingFromObject_IdObject_ToStringOfTheUuid() throws Exception {

		var value = new UserAccount();

		assertThat(om.writeValueAsString(value)).containsIgnoringWhitespaces("\"id\":\"" + value.getId().toString() + "\"");
	}

	@Test
	@DisplayName("JSON: String of an UUID ⇒ DTO: UserAccountIdentifier")
	void mappingFromJson_UuidString_ToUserAccountIdentifier() throws Exception {

		var uuid = UUID.randomUUID();

		assertThat(om.readValue("\"" + uuid.toString() + "\"", UserAccountIdentifier.class))
				.extracting(UserAccountIdentifier::toUuid)
				.isEqualTo(uuid);
	}

	@Test
	@DisplayName("JSON: String of object with UUID ⇒ DTO: UserAccount")
	void mappingFromJson_ObjectWithUuid_ToUserAccount() throws Exception {

		var uuid = UUID.randomUUID();

		assertThat(om.readValue("{\"id\":\"" + uuid.toString() + "\"}", UserAccount.class))
				.extracting(UserAccount::getId)
				.extracting(UserAccountIdentifier::toUuid)
				.isEqualTo(uuid);
	}

	@ParameterizedTest(name = "Input \"{0}\"")
	@ValueSource(strings = { "Test", "" })
	@DisplayName("JSON: Invalid  string ⇒ ConversionFailedException")
	void mappingFromJson_InvalidUuidString_ThrowsException(String invalidUuid) throws Exception {

		var exc = assertThrows(IllegalArgumentException.class,
				() -> om.readValue("\"" + invalidUuid + "\"", UserAccountIdentifier.class));

		assertThat(exc).hasMessage("Invalid UUID string: " + invalidUuid);
	}

	@Test
	@DisplayName("JSON: Null as id ⇒ DTO: UserAccount with Null")
	void mappingFromJson_ObjectWithNullId_ToUserAccount() throws Exception {

		assertThat(om.readValue("{\"id\":null}", UserAccount.class))
				.extracting(UserAccount::getId)
				.isNull();
	}

	record Sample(String value) {}
}
