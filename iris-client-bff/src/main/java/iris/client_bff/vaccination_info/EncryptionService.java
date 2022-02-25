package iris.client_bff.vaccination_info;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.util.Base64Utils.*;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Service
public class EncryptionService {

	/**
	 * 
	 */
	private static final String ALGORITHM = "AES/GCM/NoPadding";
	/**
	 * 
	 */
	private static final String PROVIDER = "BCFIPS";

	public KeyPair generateKeyPair() throws GeneralSecurityException {

		KeyPairGenerator keyPair = KeyPairGenerator.getInstance("EC", PROVIDER);
		keyPair.initialize(384);

		return keyPair.generateKeyPair();
	}

	public String encodeToBase64(PublicKey key) {
		return encodeToString(key.getEncoded());
	}

	public PublicKey decodeFromBase64(String encodedKey) throws GeneralSecurityException {

		var kf = KeyFactory.getInstance("EC", PROVIDER);

		var keySpecX509 = new X509EncodedKeySpec(decodeFromString(encodedKey));

		return kf.generatePublic(keySpecX509);
	}

	public SecretKey generateAgreedKey(PrivateKey ownPrivate, PublicKey foreignPublic)
			throws GeneralSecurityException {

		KeyAgreement agreement = KeyAgreement.getInstance("ECCDH", PROVIDER);
		agreement.init(ownPrivate);
		agreement.doPhase(foreignPublic, true);

		return agreement.generateSecret("AES[256]");
	}

	public EncryptedData encryptAndEncode(SecretKey key, String data)
			throws GeneralSecurityException {

		var iv = SecureRandom.getSeed(12);

		Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
		cipher.init(Cipher.ENCRYPT_MODE, key, // taglength, nonce
				new GCMParameterSpec(128, iv));

		return new EncryptedData(encodeToString(iv), encodeToString(cipher.doFinal(data.getBytes(UTF_8))));
	}

	public String decodeAndDecrypt(SecretKey key, String encodedIv, String encodedEncryptedData)
			throws GeneralSecurityException {

		Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
		cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, decodeFromString(encodedIv)));

		return new String(cipher.doFinal(decodeFromString(encodedEncryptedData)), UTF_8);
	}

	public record EncryptedData(String iv, String data) {};
}
