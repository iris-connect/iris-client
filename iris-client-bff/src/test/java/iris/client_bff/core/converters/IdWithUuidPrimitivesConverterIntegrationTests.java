package iris.client_bff.core.converters;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.IdWithUuid;
import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@Tag("conversion")
@DisplayName("String/UUID ⇔ IdWithUuid conversion")
@RequiredArgsConstructor
class IdWithUuidPrimitivesConverterIntegrationTests {

	private final @NonNull ConversionService conversionService;

	@Test
	@DisplayName("String: Null ⇒ IdWithUuid: Null")
	void convertStringToId_FromNull_ToNull() throws Exception {
		assertThat(conversionService.convert(null, UserAccount.UserAccountIdentifier.class)).isNull();
	}

	@Test
	@DisplayName("IdWithUuid: Null ⇒ String: Null")
	void convertIdToString_FromNull_ToNull() throws Exception {
		assertThat(conversionService.convert(null, String.class)).isNull();
	}

	@Test
	@DisplayName("IdWithUuid: Null ⇒ UUID: Null")
	void convertIdToUuid_FromNull_ToNull() throws Exception {
		assertThat(conversionService.convert(null, UUID.class)).isNull();
	}

	@Test
	@DisplayName("UUID: Valid input ⇒ IdWithUuid: Valid UserAccountIdentifier")
	void convertUuidToId_ValidInput_ToId() throws Exception {

		var uuid = UUID.randomUUID();

		assertThat(conversionService.convert(uuid, UserAccount.UserAccountIdentifier.class))
				.isInstanceOf(UserAccountIdentifier.class)
				.extracting(IdWithUuid::toUuid).isEqualTo(uuid);
	}

	@Test
	@DisplayName("String: Valid input ⇒ IdWithUuid: Valid UserAccountIdentifier")
	void convertStringToId_ValidInput() throws Exception {

		var uuid = UUID.randomUUID().toString();

		assertThat(conversionService.convert(uuid, UserAccount.UserAccountIdentifier.class))
				.isInstanceOf(UserAccountIdentifier.class)
				.extracting(IdWithUuid::toString).isEqualTo(uuid);
	}

	@Test
	@DisplayName("IdWithUuid: Valid UserAccountIdentifier ⇒ UUID")
	void convertIdToUuid_ValidInput() throws Exception {

		var id = UserAccountIdentifier.random();

		assertThat(conversionService.convert(id, UUID.class))
				.isInstanceOf(UUID.class)
				.isEqualTo(id.toUuid());
	}

	@Test
	@DisplayName("IdWithUuid: Valid UserAccountIdentifier ⇒ String")
	void convertIdToString_ValidInput() throws Exception {

		var id = UserAccountIdentifier.random();

		assertThat(conversionService.convert(id, String.class))
				.isInstanceOf(String.class)
				.isEqualTo(id.toString());
	}

	@ParameterizedTest(name = "Input \"{0}\"")
	@ValueSource(strings = { "Test", "" })
	@DisplayName("String: Invalid input ⇒ ConversionFailedException")
	void convertStringToId_InvalidInput_ThrowsException(String invalidUuid) throws Exception {

		var exc = assertThrows(ConversionFailedException.class,
				() -> conversionService.convert(invalidUuid, UserAccount.UserAccountIdentifier.class));

		assertThat(exc).hasCauseInstanceOf(IllegalArgumentException.class)
				.getCause()
				.hasMessage("Invalid UUID string: " + invalidUuid);
	}
}
