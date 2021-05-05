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
package iris.client_bff.data_request.events;

import static io.vavr.control.Option.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import io.vavr.control.Option;
import iris.client_bff.config.IrisClientProperties;
import iris.client_bff.config.IrisProperties;
import iris.client_bff.data_request.DataRequest.DataRequestIdentifier;
import iris.client_bff.data_request.DataRequest.Feature;
import iris.client_bff.data_request.DataRequest.Status;
import iris.client_bff.data_request.Location;
import iris.client_bff.data_request.events.web.dto.DataRequestDetails;
import iris.client_bff.data_request.events.web.dto.EventUpdateDTO;
import iris.client_bff.search_client.SearchClient;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.CRC32;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
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
@RequiredArgsConstructor
public class EventDataRequestService {

	private final @NonNull EventDataRequestRepository repository;

	@NonNull @Qualifier("iris-rest")
	private final RestTemplate rest;

	private final @NonNull IrisClientProperties clientProperties;
	private final @NonNull IrisProperties properties;
	private final @NonNull ModelMapper mapper;
	private final @NonNull SearchClient searchClient;

	public List<EventDataRequest> getAll() {
		// ToDo: Pagination
		return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public Optional<EventDataRequest> findById(String id) {
		return repository.findById(DataRequestIdentifier.of(UUID.fromString(id)));
	}

	public Optional<EventDataRequest> findById(UUID uuid) {
		return repository.findById(DataRequestIdentifier.of(uuid));
	}

	public EventDataRequest save(EventDataRequest request) {
		return repository.save(request);
	}

	public EventDataRequest createContactEventRequest(String refId, String name, Instant startDate,
			Option<Instant> endDate, String hdUserId) {

		return createDataRequest(refId, name, startDate, endDate, none(), none(), Option.of(hdUserId), none(), none(),
				Set.of(Feature.Contacts_Events));
	}

	public EventDataRequest createLocationRequest(String refId, String name, Instant startDate, Instant endDate,
      Option<String> comment, Option<String> requestDetails, String locationId, String providerId) {

		return createDataRequest(refId, name, startDate, Option.of(endDate), comment, requestDetails, none(),
				Option.of(locationId), Option.of(providerId), Set.of(Feature.Guests));
	}

	public EventDataRequest createLocationRequest(String refId, String name, Instant startDate, Option<Instant> endDate,
      Option<String> comment, Option<String> requestDetails, Option<String> hdUserId) {

		return createDataRequest(refId, name, startDate, endDate, comment, requestDetails, hdUserId,
				none(), none(), Set.of(Feature.Guests));
	}

	EventDataRequest createDataRequest(String refId, String name, Instant startDate, Option<Instant> endDate,
      Option<String> comment, Option<String> requestDetails, Option<String> hdUserId, Option<String> locationId,
			Option<String> providerId, Set<Feature> feature) {

		var location = fetchLocation(locationId, providerId);

		var dataRequest = new EventDataRequest(refId, name, startDate, endDate.getOrNull(), comment.getOrNull(),
        requestDetails.getOrNull(), hdUserId.getOrNull(), location, feature);

		log.trace("Request job - PUT to server is sent: {}", dataRequest.getId().toString());

		var dto = DataRequestDto.of(dataRequest, clientProperties.getClientId(), clientProperties.getRkiCode());

		var headers = new HttpHeaders();
		headers.setContentType(new MediaType(APPLICATION_JSON, UTF_8));

		rest.put("https://{address}:{port}/hd/data-requests/{id}", new HttpEntity<>(dto, headers),
				properties.getServerAddress().getHostName(), properties.getServerPort(), dataRequest.getId());

		log.debug("Request job - PUT to server sent: {}; Features = {}", dataRequest.getId().toString(),
				dataRequest.getFeatures());

		// TODO: verify that DATA_REQUESTED can be used as default
		dataRequest.setStatus(Status.DATA_REQUESTED);

		dataRequest = repository.save(dataRequest);

		return dataRequest;
	}

	/**
	 * Fetches the full location informations for the given IDs from the location service.
	 *
	 * @param locationId
	 * @param providerId
	 * @return The location entity fetched from location service
	 */
	private Location fetchLocation(Option<String> locationId, Option<String> providerId) {

		if (locationId.isEmpty() || providerId.isEmpty()) {
			return null;
		}

		return searchClient.findByProviderIdAndLocationId(providerId.get(), locationId.get());
	}

	// ToDo: I guess this will be the method for the TAN generation. So I will leave it here for now.
	private String findValidCode() {

		var code = generateCode();

		return repository.isCodeAvailable(UUID.fromString(code)) ? code : findValidCode();
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

	public EventDataRequest update(EventDataRequest dataRequest, EventUpdateDTO patch) {
		if (StringUtils.isNotEmpty(patch.getComment())) {
			dataRequest.setComment(patch.getComment());
		}
		if (StringUtils.isNotEmpty(patch.getExternalCaseId())) {
			dataRequest.setRefId(patch.getExternalCaseId());
		}
		if (StringUtils.isNotEmpty(patch.getName())) {
			dataRequest.setName(patch.getName());
		}
		if (patch.getStatus() != null) {
			var status = Status.valueOf(patch.getStatus().name());
			dataRequest.setStatus(status);
		}

		return repository.save(dataRequest);
	}

	@Data
	static class DataRequestDto {

		private final String departmentId;

		private final String locationId;
		private final String providerId;

		private final Instant requestStart;
		private final Instant requestEnd;

		private final String requestDetails;

		private final Set<Feature> features;
		private final Status status;

		static DataRequestDto of(EventDataRequest request, UUID departmentId, String rkiCode) {

			var location = request.getLocation();
			var locationId = location != null ? location.getLocationId() : null;
			var providerId = location != null ? location.getProviderId() : null;

			return new DataRequestDto(departmentId.toString(), locationId,
					providerId, request.getRequestStart(), request.getRequestEnd(),
					request.getRequestDetails(), request.getFeatures(), request.getStatus());
		}
	}
}
