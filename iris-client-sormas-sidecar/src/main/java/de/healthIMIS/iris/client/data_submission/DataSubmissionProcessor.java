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

import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.data_submission.supplier_connection.FetchedDataSubmissions;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.security.KeyStore;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
class DataSubmissionProcessor {

	private final @NonNull DataRequestManagement dataRequests;
	private final @NonNull ObjectMapper mapper;
	private final @NonNull KeyStore keyStore;
	private final @NonNull ModelMapper modelMapper;
	private final @NonNull DataSubmissionRepository submissions;

	void processSubmissions(FetchedDataSubmissions fetchedSubmissions) {
		fetchedSubmissions.map(this::mapToStrategie).forEach(DataSubmissionProcess::process);
	}

	private DataSubmissionProcess<?> mapToStrategie(DataSubmissionDto it) {

		var request = dataRequests.findById(it.getRequestId()).get();

		switch (it.getFeature()) {
			case Contacts_Events:
				return new ContactsEventsSubmissionProcess(it, request, keyStore, mapper);
			// return new ContactsEventsSubmissionProcessor(it, request, keyStore, mapper, sormasTaskApi, sormasPersonApi,
			// sormasContactApi, sormasEventApi, sormasParticipantApi);
			case Guests:
				return new GuestsSubmissionProcess(it, request, keyStore, mapper, modelMapper, submissions, dataRequests);
			// return new GuestsSubmissionProcessor(it, request, keyStore, mapper, sormasTaskApi, sormasParticipantApi,
			// sormasPersonApi);
			default:
				return null;
		}
	}
}
