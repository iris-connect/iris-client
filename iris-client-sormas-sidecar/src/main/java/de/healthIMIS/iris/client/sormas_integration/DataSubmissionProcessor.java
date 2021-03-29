/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.client.sormas_integration;

import static java.nio.charset.StandardCharsets.*;

import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.sormas_integration.DataSubmissionJob.DataSubmissionDto;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import lombok.Getter;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
@Getter
abstract class DataSubmissionProcessor<T> extends DataSubmissionSubProcessor<T> {

	static final String ENCRYPTION_ALGORITHM = "AES";
	static final String KEY_ENCRYPTION_ALGORITHM = "RSA";
	static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

	private final DataSubmissionDto submissionDto;
	private final Class<T> dtoType;
	private final KeyStore keyStore;
	private final ObjectMapper mapper;

	public DataSubmissionProcessor(DataSubmissionDto submissionDto, Class<T> dtoType, DataRequest request,
			KeyStore keyStore, ObjectMapper mapper, TaskControllerApi taskApi) {

		super(request, taskApi);

		this.submissionDto = submissionDto;
		this.dtoType = dtoType;
		this.keyStore = keyStore;
		this.mapper = mapper;
	}

	void process() {
		try {

			var content = decryptContent(submissionDto.getEncryptedData(), submissionDto.getKeyReference(),
					submissionDto.getSecret());
			var dto = mapper.readValue(content, dtoType);

			process(dto);

		} catch (JsonProcessingException | InvalidKeyException | UnrecoverableKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException
				| KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	protected String decryptContent(String content, String keyReference, String encryptedSecretKeyString)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, UnrecoverableKeyException, KeyStoreException {

		var encryptedArray = Base64.getDecoder().decode(content);
		var encryptedSecretKey = Base64.getDecoder().decode(encryptedSecretKeyString);

		var privateKey = keyStore.getKey(keyReference, null);

		var secretKey = decryptSecretKey(encryptedSecretKey, privateKey);

		return decryptText(secretKey, encryptedArray);
	}

	private byte[] decryptSecretKey(byte[] encryptedSecretKey, Key privateKey) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		var cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.PRIVATE_KEY, privateKey);

		return cipher.doFinal(encryptedSecretKey);
	}

	private String decryptText(byte[] secretKey, byte[] encryptedText) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		var originalKey = new SecretKeySpec(secretKey, 0, secretKey.length, ENCRYPTION_ALGORITHM);

		var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, originalKey);

		return new String(cipher.doFinal(encryptedText), UTF_8);
	}
}
