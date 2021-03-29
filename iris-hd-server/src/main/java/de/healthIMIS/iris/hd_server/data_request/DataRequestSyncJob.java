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
package de.healthIMIS.iris.hd_server.data_request;

import static de.healthIMIS.iris.hd_server.core.SyncTimes.DataTypes.*;

import de.healthIMIS.iris.hd_server.core.IrisHdServerProperties;
import de.healthIMIS.iris.hd_server.core.SyncTimes;
import de.healthIMIS.iris.hd_server.core.SyncTimesRepository;
import de.healthIMIS.iris.hd_server.data_request.DataRequest.Feature;
import de.healthIMIS.iris.hd_server.data_request.DataRequest.Status;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Cyclic job that submits new or changed data requests to the public server.
 * 
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@RequiredArgsConstructor
class DataRequestSyncJob {

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull DataRequestRepository requests;
	private final @NonNull IrisHdServerProperties properties;
	private final @NonNull RestTemplate rest;

	private long errorCounter = 0;

	@Scheduled(fixedDelay = 15000)
	void run() {

		log.trace("Request job - start");

		var lastSync = syncTimes.findById(Requests).map(SyncTimes::getLastSync)
				.orElse(LocalDateTime.of(2000, 01, 01, 00, 00));

		var changedData = requests.findAllByMetadataLastModifiedIsAfterOrderByMetadataLastModified(lastSync);

		try {

			changedData.stream().peek(this::submitDataRequest)
					.forEach(it -> syncTimes.save(SyncTimes.of(Requests, it.getMetadata().getLastModified())));

			errorCounter = 0;

		} catch (RestClientException e) {

			errorCounter++;

			e.printStackTrace();

			if (errorCounter == 1 || errorCounter % 20 == 0) {
				if (log.isTraceEnabled()) {
					log.warn(
							String.format("Request job - can't submit data requests to public server for %s tries!", errorCounter),
							e);
				} else {
					log.warn("Request job - can't submit data requests to public server for {} tries!", errorCounter);
				}
			}
		}
	}

	private void submitDataRequest(DataRequest dataRequest) {

		log.trace("Request job - PUT to public server is sent: {}", dataRequest.getId().toString());

		rest.put("http://{address}:{port}/hd/data-requests/{id}", DataRequestInternalOutputDto.of(dataRequest),
				properties.getPublicServerAddress().getHostName(), properties.getPublicServerPort(), dataRequest.getId());

		log.debug("Request job - PUT to public server sent: {}", dataRequest.getId().toString());
	}

	@Data
	static class DataRequestInternalOutputDto {

		private final String departmentId;

		private final Instant requestStart;
		private final Instant requestEnd;

		private final Set<Feature> features;
		private final Status status;

		static DataRequestInternalOutputDto of(DataRequest request) {

			return new DataRequestInternalOutputDto(request.getDepartmentId().toString(), request.getRequestStart(),
					request.getRequestEnd(), request.getFeatures(), request.getStatus());
		}
	}
}
