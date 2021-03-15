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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.time.ZonedDateTime;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.sormas_integration.DataSubmissionJob.DataSubmissionDto;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.TaskDto;
import de.healthIMIS.sormas.client.model.TaskStatus;
import de.healthIMIS.sormas.client.model.UserReferenceDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jens Kutzsche
 */
@RequiredArgsConstructor
@Getter
abstract class DataSubmissionProcessor {

	static final String ENCRYPTION_ALGORITHM = "AES";
	static final String KEY_ENCRYPTION_ALGORITHM = "RSA";
	static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

	protected final DataSubmissionDto submissionDto;
	protected final DataRequest request;
	protected final KeyStore keyStore;
	protected final ObjectMapper mapper;
	protected final TaskControllerApi taskApi;

	abstract void process();

	protected String decryptContent(String content, String keyReferenz, String encryptedSecretKeyString)
		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException,
		BadPaddingException, UnrecoverableKeyException, KeyStoreException {

		var encryptedArray = Base64.getDecoder().decode(content);
		var encryptedSecretKey = Base64.getDecoder().decode(encryptedSecretKeyString);

		var privateKey = keyStore.getKey(keyReferenz, null);

		var secretKey = decryptSecretKey(encryptedSecretKey, privateKey);

		return decryptText(secretKey, encryptedArray);
	}

	private byte[] decryptSecretKey(byte[] encryptedSecretKey, Key privateKey)
		throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		var cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.PRIVATE_KEY, privateKey);

		return cipher.doFinal(encryptedSecretKey);
	}

	private String decryptText(byte[] secretKey, byte[] encryptedText)
		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		var originalKey = new SecretKeySpec(secretKey, 0, secretKey.length, ENCRYPTION_ALGORITHM);

		var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, originalKey);

		return new String(cipher.doFinal(encryptedText), UTF_8);
	}

	protected TaskDto createTask(DataRequest request) {
		var newTask = new TaskDto();

		newTask.setUuid(SormasRefId.random().toString());

		var nowDate = ZonedDateTime.now().withSecond(0);
		newTask.setDueDate(nowDate.toInstant());
		newTask.setPerceivedStart(nowDate.plusDays(1).toInstant());

		newTask.setTaskStatus(TaskStatus.PENDING);

		var userRef = new UserReferenceDto();
		userRef.setUuid(request.getSormasUserId());
		newTask.setAssigneeUser(userRef);

		userRef = new UserReferenceDto();
		userRef.setUuid(request.getIrisUserId());
		newTask.setCreatorUser(userRef);
		return newTask;
	}
}
