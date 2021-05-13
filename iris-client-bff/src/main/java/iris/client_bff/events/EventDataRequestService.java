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
import iris.client_bff.events.eps.DataProviderClient;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.web.dto.EventUpdateDTO;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.eps.LocationMapper;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventDataRequestService {

	private final EventDataRequestRepository repository;

	private final SearchClient searchClient;
	private final DataProviderClient epsDataRequestClient;

	public List<EventDataRequest> getAll() {
		// ToDo: Pagination
		return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public Optional<EventDataRequest> findById(UUID uuid) {
		return repository.findById(DataRequestIdentifier.of(uuid));
	}

	public EventDataRequest save(EventDataRequest request) {
		return repository.save(request);
	}

	public EventDataRequest createDataRequest(String refId, String name, Instant startDate, Option<Instant> endDate,
			Option<String> comment, Option<String> requestDetails, Option<String> hdUserId, Option<String> locationId,
			Option<String> providerId) {

		Location location = null;
		try {
			if (locationId.isDefined() && providerId.isDefined()) {
				location = LocationMapper
						.map(searchClient.findByProviderIdAndLocationId(providerId.get(), locationId.get()));
			}
		} catch (IRISSearchException e) {
			// TODO handle
			throw new RuntimeException(e);
		}

		var dataRequest = new EventDataRequest(refId, name, startDate, endDate.getOrNull(), comment.getOrNull(),
				requestDetails.getOrNull(), hdUserId.getOrNull(), location);

		log.trace("Request job - PUT to server is sent: {}", dataRequest.getId().toString());

		try {
			dataRequest.setStatus(Status.DATA_REQUESTED);
			dataRequest = repository.save(dataRequest);
			epsDataRequestClient.requestGuestListData(dataRequest);
		} catch (IRISDataRequestException e) {
			// TODO delete request?
			e.printStackTrace();
		}

		log.debug("Request job - PUT to server sent: {}", dataRequest.getId().toString());

		return dataRequest;
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
