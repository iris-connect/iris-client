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

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

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

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.zip.CRC32;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jens Kutzsche
 */
@Slf4j
@Service
public class DataRequestManagement {

	private final @NonNull DataRequestRepository requests;
	private final @NonNull RestTemplate rest;
	private final @NonNull IrisClientProperties clientProperties;
	private final @NonNull IrisProperties properties;

	public DataRequestManagement(@NonNull DataRequestRepository requests,
			@NonNull @Qualifier("iris-rest") RestTemplate rest, @NonNull IrisClientProperties clientProperties,
			@NonNull IrisProperties properties) {

		this.requests = requests;
		this.rest = rest;
		this.clientProperties = clientProperties;
		this.properties = properties;
	}

	public Optional<DataRequest> findById(String id) {
		return requests.findById(DataRequestIdentifier.of(UUID.fromString(id)));
	}

	public Optional<DataRequest> findById(UUID uuid) {
		return requests.findById(DataRequestIdentifier.of(uuid));
	}

	public DataRequest createContactEventRequest(SormasRefId refId, SormasRefId personId, Instant startDate,
			Option<Instant> endDate, String irisUserId, String sormasUserId) {

		return createDataRequest(refId, Option.of(personId), Option.of(startDate), endDate, Option.none(), irisUserId,
				sormasUserId, Set.of(Feature.Contacts_Events));
	}

	public DataRequest createGuestsRequest(SormasRefId refId, Instant startDate, Option<Instant> endDate,
			String requestDetails, String irisUserId, String sormasUserId) {

		return createDataRequest(refId, Option.none(), Option.of(startDate), endDate, Option.of(requestDetails), irisUserId,
				sormasUserId, Set.of(Feature.Guests));
	}

	DataRequest createDataRequest(SormasRefId refId, Option<SormasRefId> personId, Option<Instant> startDate,
			Option<Instant> endDate, Option<String> requestDetails, String irisUserId, String sormasUserId,
			Set<Feature> feature) {

		var dataRequest = new DataRequest(refId, personId.getOrNull(), startDate.getOrNull(), endDate.getOrNull(),
				requestDetails.getOrNull(), irisUserId, sormasUserId, feature);

		log.trace("Request job - PUT to server is sent: {}", dataRequest.getId().toString());

		var dto = DataRequestDto.of(dataRequest, clientProperties.getClientId(), clientProperties.getRkiCode());

		var headers = new HttpHeaders();
		headers.setContentType(new MediaType(APPLICATION_JSON, UTF_8));

		rest.put("https://{address}:{port}/hd/data-requests/{id}", new HttpEntity<>(dto, headers),
				properties.getServerAddress().getHostName(), properties.getServerPort(), dataRequest.getId());

		log.debug("Request job - PUT to server sent: {}; Features = {}", dataRequest.getId().toString(),
				dataRequest.getFeatures());

		dataRequest = requests.save(dataRequest);

		return dataRequest;
	}

	private String findValidCode() {

		var code = generateCode();

		return requests.isCodeAvailable(UUID.fromString(code)) ? code : findValidCode();
	}

	private String generateCode() {

		var code = RandomStringUtils.random(9, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'K', 'M', 'N', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9');

		var bytes = code.getBytes();
		var crc32 = new CRC32();
		crc32.update(bytes, 0, bytes.length);
		var sum = crc32.getValue() % 16;

		return code + Long.toHexString(sum).toUpperCase();
	}

	@Data
	static class DataRequestDto {

		private final String departmentId;

		private final Instant requestStart;
		private final Instant requestEnd;

		private final String requestDetails;

		private final Set<Feature> features;
		private final Status status;

		static DataRequestDto of(DataRequest request, UUID departmentId, String rkiCode) {

			return new DataRequestDto(departmentId.toString(), request.getRequestStart(), request.getRequestEnd(),
					request.getRequestDetails(), request.getFeatures(), request.getStatus());
		}
	}
}
