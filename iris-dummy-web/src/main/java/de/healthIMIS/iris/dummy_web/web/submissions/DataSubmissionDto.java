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
package de.healthIMIS.iris.dummy_web.web.submissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic data type of a data submission which contains the unencrypted metadata needed for processing.
 */
public class DataSubmissionDto {

	@JsonProperty("checkCode")
	private List<String> checkCode = new ArrayList<String>();

	@JsonProperty("secret")
	private String secret = null;

	@JsonProperty("keyReferenz")
	private String keyReferenz = null;

	@JsonProperty("encryptedData")
	private String encryptedData = null;

	public DataSubmissionDto encryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
		return this;
	}

	/**
	 * Get encryptedData
	 * 
	 * @return encryptedData
	 **/
	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public DataSubmissionDto checkCode(List<String> checkCode) {
		this.checkCode = checkCode;
		return this;
	}

	public DataSubmissionDto addCheckCodeItem(String checkCodeItem) {
		this.checkCode.add(checkCodeItem);
		return this;
	}

	/**
	 * An array of up to three check codes, one of them must be correct for the data to be accepted. For details, please
	 * refer to section \"Prüfcode\" in concept at https://github.com/healthIMIS/IRIS-Concept/releases/latest
	 * 
	 * @return checkCode
	 **/
	public List<String> getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(List<String> checkCode) {
		this.checkCode = checkCode;
	}

	public DataSubmissionDto secret(String secret) {
		this.secret = secret;
		return this;
	}

	/**
	 * The encrypted secret key for encryption.
	 * 
	 * @return secret
	 **/
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public DataSubmissionDto keyReferenz(String keyReferenz) {
		this.keyReferenz = keyReferenz;
		return this;
	}

	/**
	 * Reference to the used key.
	 * 
	 * @return keyReferenz
	 **/
	public String getKeyReferenz() {
		return keyReferenz;
	}

	public void setKeyReferenz(String keyReferenz) {
		this.keyReferenz = keyReferenz;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DataSubmissionDto dataSubmission = (DataSubmissionDto) o;
		return Objects.equals(this.checkCode, dataSubmission.checkCode)
				&& Objects.equals(this.secret, dataSubmission.secret)
				&& Objects.equals(this.keyReferenz, dataSubmission.keyReferenz);
	}

	@Override
	public int hashCode() {
		return Objects.hash(checkCode, secret, keyReferenz);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DataSubmission {\n");

		sb.append("    checkCode: ").append(toIndentedString(checkCode)).append("\n");
		sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
		sb.append("    keyReferenz: ").append(toIndentedString(keyReferenz)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
