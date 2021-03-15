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
package de.healthIMIS.iris.client.sormas_integration;

import java.security.KeyStore;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.healthIMIS.iris.client.core.IrisClientProperties;
import de.healthIMIS.iris.client.core.IrisProperties;
import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.sormas.client.api.CaseControllerApi;
import de.healthIMIS.sormas.client.api.ContactControllerApi;
import de.healthIMIS.sormas.client.api.EventControllerApi;
import de.healthIMIS.sormas.client.api.EventParticipantControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.SampleControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@Profile("!inttest")
class DataSubmissionJob {

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull CaseControllerApi sormasCaseApi;
	private final @NonNull PersonControllerApi sormasPersonApi;
	private final @NonNull TaskControllerApi sormasTaskApi;
	private final @NonNull SampleControllerApi sormasSampleApi;
	private final @NonNull ContactControllerApi sormasContactApi;
	private final @NonNull EventControllerApi sormasEventApi;
	private final @NonNull EventParticipantControllerApi sormasParticipantApi;
	private final @NonNull DataRequestManagement dataRequests;
	private final @NonNull RestTemplate rest;
	private final @NonNull IrisClientProperties clientProperties;
	private final @NonNull IrisProperties properties;
	private final @NonNull ObjectMapper mapper;
	private final @NonNull KeyStore keyStore;

	private long errorCounter = 0;

	public DataSubmissionJob(
		@NonNull SyncTimesRepository syncTimes,
		@NonNull CaseControllerApi sormasCaseApi,
		@NonNull PersonControllerApi sormasPersonApi,
		@NonNull TaskControllerApi sormasTaskApi,
		@NonNull SampleControllerApi sormasSampleApi,
		@NonNull ContactControllerApi sormasContactApi,
		@NonNull DataRequestManagement dataRequests,
		@NonNull EventControllerApi sormasEventApi,
		@NonNull EventParticipantControllerApi participantControllerApi,
		@NonNull @Qualifier("iris-rest") RestTemplate rest,
		@NonNull IrisClientProperties clientProperties,
		@NonNull IrisProperties properties,
		@NonNull ObjectMapper mapper,
		@NonNull KeyStore keyStore) {

		this.syncTimes = syncTimes;
		this.sormasCaseApi = sormasCaseApi;
		this.sormasPersonApi = sormasPersonApi;
		this.sormasTaskApi = sormasTaskApi;
		this.sormasSampleApi = sormasSampleApi;
		this.sormasContactApi = sormasContactApi;
		this.sormasEventApi = sormasEventApi;
		this.sormasParticipantApi = participantControllerApi;
		this.dataRequests = dataRequests;
		this.rest = rest;
		this.clientProperties = clientProperties;
		this.properties = properties;
		this.mapper = mapper;
		this.keyStore = keyStore;
	}

	@Scheduled(fixedDelay = 15000)
	void run() {

		log.trace("Submission job - start");

		var lastSync = syncTimes.findById(SyncTimes.DataTypes.Submissions)
			.map(SyncTimes::getLastSync)
			.map(it -> it.atZone(ZoneId.systemDefault()).toLocalDateTime())
			.orElse(LocalDateTime.of(2000, 01, 01, 00, 00));

		try {

			log.trace("Submission job - GET to server is sent");

			var response = rest.getForEntity(
				"https://{address}:{port}/hd/data-submissions?departmentId={depId}&from={from}",
				DataSubmissionDto[].class,
				properties.getServerAddress().getHostName(),
				properties.getServerPort(),
				clientProperties.getClientId(),
				lastSync);

			handleDtos(response.getBody());

			saveLastSync(response);

			log.debug(
				"Submission job - GET to public server sent: {}",
				Arrays.stream(response.getBody()).map(it -> it.getRequestId().toString()).collect(Collectors.joining(", ")));

			callDeleteEndpoint(lastSync);

			errorCounter = 0;

		} catch (RestClientException e) {

			errorCounter++;

			e.printStackTrace();

			if (errorCounter == 1 || errorCounter % 20 == 0) {
				if (log.isTraceEnabled()) {
					log.warn(String.format("Submission job - can't submit data submissions from server for %s tries!", errorCounter), e);
				} else {
					log.warn("Submission job - can't submit data submissions from server for {} tries!", errorCounter);
				}
			}
		}
	}

	private void handleDtos(DataSubmissionDto[] dtos) {
		Arrays.stream(dtos).map(this::mapToStrategie).forEach(DataSubmissionProcessor::process);
	}

	private DataSubmissionProcessor mapToStrategie(DataSubmissionDto it) {

		var request = dataRequests.findById(it.getRequestId()).get();

		switch (it.feature) {
		case Contacts:
			return new ContactsSubmissionProcessor(it, request, keyStore, mapper, sormasTaskApi, sormasPersonApi, sormasContactApi);
		case Events:
			return new EventsSubmissionProcessor(it, request, keyStore, mapper, sormasTaskApi, sormasEventApi, sormasParticipantApi, sormasPersonApi);
		case Guests:
		default:
			return null;
		}
	}

	private void saveLastSync(ResponseEntity<DataSubmissionDto[]> response) {

		var lastModified = response.getHeaders().getLastModified();

		var lastSync = Instant.ofEpochMilli(lastModified);

		syncTimes.save(SyncTimes.of(SyncTimes.DataTypes.Submissions, lastSync));
	}

	private void callDeleteEndpoint(LocalDateTime lastSync) {

		log.trace("Submission job - DELETE to public server is sent");

		rest.delete(
			"https://{address}:{port}/hd/data-submissions?departmentId={depId}&from={from}",
			properties.getServerAddress().getHostName(),
			properties.getServerPort(),
			clientProperties.getClientId(),
			lastSync);

		log.trace("Submission job - DELETE to public server sent");
	}

	@Data
	static class DataSubmissionDto {

		private final UUID id;

		private final UUID requestId;

		private final UUID departmentId;

		private final String secret;

		private final String keyReferenz;

		private final String encryptedData;

		private final Feature feature;
	}

	public enum Feature {
		Contacts,
		Events,
		Guests
	}
}
