/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.client.data_submission;

import de.healthIMIS.iris.client.core.sync.SyncTimes;
import de.healthIMIS.iris.client.core.sync.SyncTimesRepository;
import de.healthIMIS.iris.client.data_submission.supplier_connection.DataSubmissionEndpointConnector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

/**
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@Profile("!inttest")
@RequiredArgsConstructor
class DataSubmissionJob {

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull DataSubmissionEndpointConnector connector;
	private final @NonNull DataSubmissionProcessor processor;

	private long errorCounter = 0;

	@Scheduled(fixedDelayString = "${iris.data-submission.fetch-interval}")
	void run() {

		log.trace("Submission job - start");

		var lastSync = syncTimes.findById(SyncTimes.DataTypes.Submissions).map(SyncTimes::getLastSync)
				.orElse(Instant.ofEpochSecond(0));

		try {

			log.trace("Submission job - GET to server is sent");
			var fetchedSubmissions = connector.fetchDataSubmissions(lastSync);

			processor.processSubmissions(fetchedSubmissions);

			saveLastSync(fetchedSubmissions.getLastModified());

			log.debug("Submission job - GET to public server sent: {}",
					fetchedSubmissions.stream()
							.map(DataSubmissionDto::getRequestId)
							.map(UUID::toString)
							.collect(Collectors.joining(", ")));

			connector.deleteDataSubmissionFromServer(fetchedSubmissions.toList());

			errorCounter = 0;

		} catch (RestClientException e) {

			errorCounter++;

			e.printStackTrace();

			if (errorCounter == 1 || errorCounter % 20 == 0) {
				if (log.isTraceEnabled()) {
					log.warn(
							String.format("Submission job - can't submit data submissions from server for %s tries!", errorCounter),
							e);
				} else {
					log.warn("Submission job - can't submit data submissions from server for {} tries!", errorCounter);
				}
			}
		}
	}

	private void saveLastSync(Instant lastSync) {
		syncTimes.save(SyncTimes.of(SyncTimes.DataTypes.Submissions, lastSync));
	}
}
