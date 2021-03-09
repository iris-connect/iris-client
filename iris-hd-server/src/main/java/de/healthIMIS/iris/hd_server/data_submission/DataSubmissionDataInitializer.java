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

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Component;

import de.healthIMIS.iris.hd_server.core.DataInitializer;
import de.healthIMIS.iris.hd_server.data_request.DataRequestDataInitializer;
import de.healthIMIS.iris.hd_server.data_submission.DataSubmission.DataSubmissionIdentifier;
import de.healthIMIS.iris.hd_server.data_submission.DataSubmission.Feature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
class DataSubmissionDataInitializer implements DataInitializer {

	static final DataSubmissionIdentifier SUBMISSION_ID_1 = DataSubmissionIdentifier.of(UUID.fromString("6ad7760e-e6cf-4849-992f-a658a8f938e3"));
	static final DataSubmissionIdentifier SUBMISSION_ID_2 = DataSubmissionIdentifier.of(UUID.fromString("eea45b6e-b0fb-4f30-9563-575b1de10774"));

	private final DataSubmissionRepository submissions;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		log.debug("Test data: creating data submissions …");

		var list = new ArrayList<DataSubmission>();

		list.add(
			new DataSubmission(
				SUBMISSION_ID_1,
				DataRequestDataInitializer.REQ_ID_1,
				DataRequestDataInitializer.DEPARTMENT_ID_1,
				"salt",
				"key",
				"DATA OF CONTACTS",
				Feature.Contacts));

		list.add(
			new DataSubmission(
				SUBMISSION_ID_2,
				DataRequestDataInitializer.REQ_ID_1,
				DataRequestDataInitializer.DEPARTMENT_ID_1,
				"salt",
				"key",
				"DATA OF EVENTS",
				Feature.Events));

		submissions.saveAll(list);
	}
}
