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

import static de.healthIMIS.iris.hd_server.core.SyncTimes.DataTypes.Submissions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.healthIMIS.iris.hd_server.core.DepartmentIdentifier;
import de.healthIMIS.iris.hd_server.core.IrisHdServerProperties;
import de.healthIMIS.iris.hd_server.core.SyncTimes;
import de.healthIMIS.iris.hd_server.core.SyncTimesRepository;
import de.healthIMIS.iris.hd_server.data_request.DataRequest.DataRequestIdentifier;
import de.healthIMIS.iris.hd_server.data_submission.DataSubmission.DataSubmissionIdentifier;
import de.healthIMIS.iris.hd_server.data_submission.DataSubmission.Feature;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@RequiredArgsConstructor
class DataSubmissionSyncJob {

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull DataSubmissionRepository submissions;
	private final @NonNull IrisHdServerProperties properties;
	private final @NonNull RestTemplate rest;
	private final @NonNull ObjectMapper mapper;

	private long errorCounter = 0;

	@Scheduled(fixedDelay = 15000)
	void run() {

		log.trace("Submission job - start");

		var lastSync = syncTimes.findById(Submissions).map(SyncTimes::getLastSync).orElse(LocalDateTime.of(2000, 01, 01, 00, 00));

		try {

			log.trace("Submission job - GET to public server is sent");

			var response = rest.getForEntity(
				"http://{address}:{port}/hd/data-submissions?from={from}",
				DataSubmissionInternalInputDto[].class,
				properties.getPublicServerAddress().getHostName(),
				properties.getPublicServerPort(),
				lastSync);

			var dtos = response.getBody();

			var dataSubmissions = Arrays.stream(dtos)
				.map(
					it -> new DataSubmission(
						DataSubmissionIdentifier.of(it.id),
						DataRequestIdentifier.of(it.requestId),
						DepartmentIdentifier.of(it.departmentId),
						it.salt,
						it.keyReferenz,
						it.encryptedData,
						it.feature))
				.collect(Collectors.toList());

			submissions.deleteAll(dataSubmissions);
			submissions.saveAll(dataSubmissions);

			saveLastSync(response);

			log.debug(
				"Submission job - GET to public server sent: {}",
				Arrays.stream(dtos).map(it -> it.getRequestId().toString()).collect(Collectors.joining(", ")));

			callDeleteEndpoint(lastSync);

			errorCounter = 0;

		} catch (RestClientException e) {

			errorCounter++;

			e.printStackTrace();

			if (errorCounter == 1 || errorCounter % 20 == 0) {
				if (log.isTraceEnabled()) {
					log.warn(String.format("Submission job - can't submit data submissions from public server for %s tries!", errorCounter), e);
				} else {
					log.warn("Submission job - can't submit data submissions from public server for {} tries!", errorCounter);
				}
			}
		}
	}

	private void saveLastSync(ResponseEntity<DataSubmissionInternalInputDto[]> response) {

		var lastModified = response.getHeaders().getLastModified();

		var lastSync = Instant.ofEpochMilli(lastModified).atZone(ZoneId.systemDefault()).toLocalDateTime();

		syncTimes.save(SyncTimes.of(Submissions, lastSync));
	}

	private void callDeleteEndpoint(LocalDateTime lastSync) {

		log.trace("Submission job - DELETE to public server is sent");

		rest.delete(
			"http://{address}:{port}/hd/data-submissions?from={from}",
			properties.getPublicServerAddress().getHostName(),
			properties.getPublicServerPort(),
			lastSync);

		log.trace("Submission job - DELETE to public server sent");
	}

	@Data
	static class DataSubmissionInternalInputDto {

		private final UUID id;

		private final UUID requestId;

		private final UUID departmentId;

		private final String salt;

		private final String keyReferenz;

		private final String encryptedData;

		private final Feature feature;
	}
}
