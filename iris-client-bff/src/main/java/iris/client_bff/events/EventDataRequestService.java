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
package iris.client_bff.events;

import io.vavr.control.Option;
import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.connection.DataRequestEndpointConnector;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.web.dto.EventUpdateDTO;
import iris.client_bff.search_client.SearchClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.CRC32;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
public class EventDataRequestService {

  private final @NonNull EventDataRequestRepository repository;

  private final DataRequestEndpointConnector connector;

  private final @NonNull ModelMapper mapper;
  private final @NonNull SearchClient searchClient;

  public List<EventDataRequest> getAll() {
	// ToDo: Pagination
	return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  public Optional<EventDataRequest> findById(String id) {
	return findById(UUID.fromString(id));
  }

  public Optional<EventDataRequest> findById(UUID uuid) {
	return repository.findById(DataRequestIdentifier.of(uuid));
  }

  public EventDataRequest save(EventDataRequest request) {
	return repository.save(request);
  }

  public EventDataRequest createLocationRequest(String refId, String name, Instant startDate, Instant endDate,
	  Option<String> comment, Option<String> requestDetails, String locationId, String providerId) {

	var location = fetchLocation(locationId, providerId);

	var dataRequest = new EventDataRequest(refId, name, startDate, endDate, comment.getOrNull(),
		requestDetails.getOrNull(), null, location);

	connector.sendDataRequestToServer(dataRequest);

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
  private Location fetchLocation(String locationId, String providerId) {
	return searchClient.findByProviderIdAndLocationId(providerId, locationId);
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
	if (StringUtils.isNotEmpty(patch.getExternalRequestId())) {
	  dataRequest.setRefId(patch.getExternalRequestId());
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
}
