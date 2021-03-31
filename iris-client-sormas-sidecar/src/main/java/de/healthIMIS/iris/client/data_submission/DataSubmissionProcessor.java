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
package de.healthIMIS.iris.client.data_submission;

import de.healthIMIS.iris.client.data_request.DataRequest;
import lombok.Getter;

import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
@Getter
public abstract class DataSubmissionProcessor<T> {

	static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
	static final String KEY_ENCRYPTION_ALGORITHM = "RSA";
	static final String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

	private final DataSubmissionDto submissionDto;
	private final Class<T> dtoType;
	private final KeyStore keyStore;
	private final ObjectMapper mapper;
	private final DataRequest request;

	public DataSubmissionProcessor(DataSubmissionDto submissionDto, Class<T> dtoType, DataRequest request,
			KeyStore keyStore, ObjectMapper mapper) {

		// super(request, taskApi);

		this.submissionDto = submissionDto;
		this.dtoType = dtoType;
		this.request = request;
		this.keyStore = keyStore;
		this.mapper = mapper;
	}

	public abstract void process(T dto);

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

		var privateKey = keyStore.getKey(keyReference, null);

		var decryptor = new Decryptor(encryptedSecretKeyString, privateKey, content);

		return decryptor.decrypt();
	}
}
