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
package de.healthIMIS.iris.client.data_request;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.healthIMIS.iris.client.core.IrisClientDevProperties;
import de.healthIMIS.iris.client.core.IrisClientProperties;
import de.healthIMIS.iris.client.core.IrisProperties;
import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest.DataRequestIdentifier;
import de.healthIMIS.iris.client.data_request.DataRequest.Feature;
import de.healthIMIS.iris.client.data_request.DataRequest.Status;
import io.vavr.control.Option;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jens Kutzsche
 */
@Slf4j
@Service
public class DataRequestManagement {

	private final @NonNull DataRequestRepository requests;
	private final @NonNull RestTemplate rest;
	private final @NonNull IrisClientProperties clientProperties;
	private final @NonNull IrisClientDevProperties devProperties;
	private final @NonNull IrisProperties properties;

	public DataRequestManagement(
		@NonNull DataRequestRepository requests,
		@NonNull @Qualifier("iris-rest") RestTemplate rest,
		@NonNull IrisClientProperties clientProperties,
		@Nullable IrisClientDevProperties devProperties,
		@NonNull IrisProperties properties) {

		this.requests = requests;
		this.rest = rest;
		this.clientProperties = clientProperties;
		this.devProperties = devProperties;
		this.properties = properties;
	}

	public Optional<DataRequest> findById(String id) {
		return requests.findById(DataRequestIdentifier.of(UUID.fromString(id)));
	}

	public Optional<DataRequest> findById(UUID uuid) {
		return requests.findById(DataRequestIdentifier.of(uuid));
	}

	public DataRequest createContactEventRequest(
		SormasRefId refId,
		String checkCodeName,
		Option<String> checkCodeBirthday,
		Instant startDate,
		Option<Instant> endDate,
		String irisUserId,
		String sormasUserId) {

		return createDataRequest(
			refId,
			Option.of(checkCodeName),
			checkCodeBirthday,
			Option.of(startDate),
			endDate,
			irisUserId,
			sormasUserId,
			Set.of(Feature.Contacts, Feature.Events));
	}

	DataRequest createDataRequest(
		SormasRefId refId,
		Option<String> checkCode1,
		Option<String> checkCode2,
		Option<Instant> startDate,
		Option<Instant> endDate,
		String irisUserId,
		String sormasUserId,
		Set<Feature> feature) {

		var dataRequest = new DataRequest(
			refId,
			checkCode1.getOrNull(),
			checkCode2.getOrNull(),
			determineRandomCheckCode(),
			startDate.getOrNull(),
			endDate.getOrNull(),
			irisUserId,
			sormasUserId,
			feature);

		log.trace("Request job - PUT to server is sent: {}", dataRequest.getId().toString());

		var dto = DataRequestDto.of(dataRequest, clientProperties.getClientId());

		rest.put(
			"https://{address}:{port}/hd/data-requests/{id}",
			dto,
			properties.getServerAddress().getHostName(),
			properties.getServerPort(),
			dataRequest.getId());

		log.debug("Request job - PUT to server sent: {}", dataRequest.getId().toString());

		dataRequest = requests.save(dataRequest);

		return dataRequest;
	}

	private String determineRandomCheckCode() {
		return Optional.ofNullable(devProperties).map(IrisClientDevProperties::getRandomCheckcode).orElseGet(() -> randomAlphabetic(10));
	}

	@Data
	static class DataRequestDto {

		private final String departmentId;

		private final String checkCodeName;
		private final String checkCodeDayOfBirth;
		private final String checkCodeRandom;

		private final Instant requestStart;
		private final Instant requestEnd;

		private final Set<Feature> features;
		private final Status status;

		static DataRequestDto of(DataRequest request, UUID departmentId) {

			return new DataRequestDto(
				departmentId.toString(),
				request.getCheckCodeOne(),
				request.getCheckCodeTwo(),
				request.getCheckCodeRandom(),
				request.getRequestStart(),
				request.getRequestEnd(),
				request.getFeatures(),
				request.getStatus());
		}
	}
}
