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
package de.healthIMIS.iris.hd_server.data_submission;

import de.healthIMIS.iris.hd_server.core.Aggregate;
import de.healthIMIS.iris.hd_server.core.DepartmentIdentifier;
import de.healthIMIS.iris.hd_server.core.Id;
import de.healthIMIS.iris.hd_server.data_request.DataRequest.DataRequestIdentifier;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * A data submission from an app of a citizen or event/location operator to the health department.
 * 
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "data_submission")
@Inheritance
@DiscriminatorColumn(name = "submission_type")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
@Setter(AccessLevel.PACKAGE)
public class DataSubmission extends Aggregate<DataSubmission, DataSubmission.DataSubmissionIdentifier> {

	private DataRequestIdentifier requestId;
	private DepartmentIdentifier departmentId;

	private String salt;
	private String keyReference;
	private @Lob String encryptedData;

	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private Feature feature;

	public DataSubmission(DataSubmissionIdentifier id, DataRequestIdentifier requestId, DepartmentIdentifier departmentId,
			String salt, String keyReference, String encryptedData, Feature feature) {

		super();

		this.id = id;
		this.requestId = requestId;
		this.departmentId = departmentId;
		this.salt = salt;
		this.keyReference = keyReference;
		this.encryptedData = encryptedData;
		this.feature = feature;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class DataSubmissionIdentifier implements Id, Serializable {

		private static final long serialVersionUID = -8254677010830428881L;

		final UUID submissionId;

		@Override
		public String toString() {
			return submissionId.toString();
		}
	}

	public enum Feature {
		Contacts, Events, Guests
	}
}
