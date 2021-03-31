package de.healthIMIS.iris.client.data_submission;

import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
public class Decryptor {

	private final String encryptedSecretKeyB64;

	private SecretKey secretKey;

	private byte[] iv;

	private final String cipherText;

	private String clearText;

	private Key privateKey;

	public Decryptor(String encryptedSecretKeyB64, Key privateKey, String cipherText) {

		this.encryptedSecretKeyB64 = encryptedSecretKeyB64;
		this.privateKey = privateKey;
		this.cipherText = cipherText;
	}

	public String decrypt() {

		log.info("Decrypting...");
		try {
			decryptEncryptedSecretKeyB64();

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			byte[] cipherBytes = Base64.getDecoder().decode(cipherText);

			iv = Arrays.copyOfRange(cipherBytes, 0, 16);

			cipherBytes = Arrays.copyOfRange(cipherBytes, 16, cipherBytes.length);

			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
			clearText = new String(cipher.doFinal(cipherBytes));

			return clearText;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void decryptEncryptedSecretKeyB64() {

		try {

			Cipher decoder = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
			decoder.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decodedKey = decoder.doFinal(Base64.getDecoder().decode(encryptedSecretKeyB64));
			secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
