package iris.client_bff.vaccination_info;

import static org.assertj.core.api.Assertions.*;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.Security;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author Jens Kutzsche
 */
@Tag("encryption")
@DisplayName("Test the encryption service for vaccination info")
class EncryptionServiceTests {

	EncryptionService encryptionService;
	KeyPair keyPair;

	@BeforeEach
	void init() throws GeneralSecurityException {

		Security.addProvider(new BouncyCastleFipsProvider());
		Security.setProperty("crypto.policy", "unlimited");

		encryptionService = new EncryptionService();
		keyPair = encryptionService.generateKeyPair();
	}

	@Test
	@DisplayName("Generate key pair successful")
	void generateKeyPair_successful() {

		assertThat(keyPair.getPrivate()).isNotNull();
		assertThat(keyPair.getPublic()).isNotNull();
	}

	@Test
	@DisplayName("Encode public key to Base64 successful")
	void encode_successful() throws GeneralSecurityException {

		var origKey = keyPair.getPublic();

		var string = encryptionService.encodeToBase64(origKey);

		assertThat(Base64.isBase64(string)).isTrue();
		assertThat(string).isEqualTo(Base64.encodeBase64String(origKey.getEncoded()));
	}

	@Test
	@DisplayName("Decode public key from Base64 successful")
	void decode_successful() throws GeneralSecurityException {

		var origKey = keyPair.getPublic();

		var key = encryptionService.decodeFromBase64(Base64.encodeBase64String(origKey.getEncoded()));

		assertThat(key).isNotNull().isEqualTo(origKey);
	}

	@Test
	@DisplayName("Generate agreed keys from key pairs successful")
	void generateAgreedKey_successful() throws GeneralSecurityException {

		var keyPair2 = encryptionService.generateKeyPair();

		var key1 = encryptionService.generateAgreedKey(keyPair.getPrivate(), keyPair2.getPublic());
		var key2 = encryptionService.generateAgreedKey(keyPair2.getPrivate(), keyPair.getPublic());

		assertThat(key1).isNotNull().isEqualTo(key2);
	}

	@Test
	@DisplayName("Encrypt and decrypt successful")
	void encryptAndDecrypt_successful() throws GeneralSecurityException {

		var key = encryptionService.generateAgreedKey(keyPair.getPrivate(), keyPair.getPublic());

		var encrypted = encryptionService.encryptAndEncode(key, "Test");
		var decrypted = encryptionService.decodeAndDecrypt(key, encrypted.iv(), encrypted.data());

		assertThat(encrypted).isNotNull().isNotEqualTo("Test");
		assertThat(decrypted).isEqualTo("Test");
	}
}
