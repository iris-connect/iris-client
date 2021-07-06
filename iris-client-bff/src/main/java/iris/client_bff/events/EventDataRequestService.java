package iris.client_bff.events;

import static iris.client_bff.search_client.eps.LocationMapper.*;
import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.eps.DataProviderClient;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.web.dto.DataRequestClient;
import iris.client_bff.events.web.dto.EventStatusDTO;
import iris.client_bff.events.web.dto.EventUpdateDTO;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	private final ProxyServiceClient proxyClient;
	private final DataProviderClient epsDataRequestClient;
	private final EventEmailProvider eventEmailProvider;

	public Page<EventDataRequest> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Page<EventDataRequest> findByStatus(Status status, Pageable pageable) {
		return repository.findByStatus(status, pageable);
	}

	public Page<EventDataRequest> findByStatusAndSearchByRefIdOrName(Status status, String search, Pageable pageable) {
		return repository.findByStatusAndSearchByRefIdOrName(status, search, pageable);
	}

	public Page<EventDataRequest> searchByRefIdOrName(String search, Pageable pageable) {
		return repository.findByRefIdContainsOrNameContainsAllIgnoreCase(search, search, pageable);
	}

	public Optional<EventDataRequest> findById(UUID uuid) {
		return repository.findById(DataRequestIdentifier.of(uuid));
	}

	public EventDataRequest save(EventDataRequest request) {
		return repository.save(request);
	}

	public EventDataRequest createDataRequest(DataRequestClient request) throws IRISDataRequestException {

		var providerId = request.getProviderId();
		var locationId = request.getLocationId();

		Location location = null;
		try {
			if (isNotEmpty(providerId) && isNotEmpty(locationId)) {
				location = map(searchClient.findByProviderIdAndLocationId(providerId, locationId));
			}
		} catch (IRISSearchException e) {
			log.error("Location {} with provider {} could not be obtained: {}", locationId, providerId, e);

			throw new IRISDataRequestException(e);
		}

		String announcementToken;
		try {
			announcementToken = proxyClient.announce();
		} catch (IRISAnnouncementException e) {
			e.printStackTrace();

			throw new IRISDataRequestException(e);
		}

		var dataRequest = new EventDataRequest(
			request.getExternalRequestId(),
			request.getName(),
			request.getStart(),
			request.getEnd(),
			request.getComment(),
			request.getRequestDetails(),
			null,
			location,
			announcementToken);

		try {
			dataRequest.setStatus(Status.DATA_REQUESTED);
			dataRequest = repository.save(dataRequest);
			epsDataRequestClient.requestGuestListData(dataRequest);

			log.info("Event Data Request submit success: {}", dataRequest.getId().toString());

		} catch (IRISDataRequestException e) {
			log.error("Event Data Request {} could not be submitted: {}", dataRequest.getId(), e);

			repository.delete(dataRequest);

			throw new IRISDataRequestException(e);
		}

		return dataRequest;
	}

	public EventDataRequest update(EventDataRequest dataRequest, EventUpdateDTO patch) {
		if (isNotEmpty(patch.getComment())) {
			dataRequest.setComment(patch.getComment());
		}
		if (isNotEmpty(patch.getExternalRequestId())) {
			dataRequest.setRefId(patch.getExternalRequestId());
		}
		if (isNotEmpty(patch.getName())) {
			dataRequest.setName(patch.getName());
		}
		if (patch.getStatus() != null) {
			var status = Status.valueOf(patch.getStatus().name());
			dataRequest.setStatus(status);

			try {
				proxyClient.abortAnnouncement(dataRequest.getAnnouncementToken());
				epsDataRequestClient.abortGuestListDataRequest(dataRequest);
			} catch (IRISAnnouncementException | IRISDataRequestException e) {
				e.printStackTrace();
				// TODO: Should we do something here?
			}
		}

		return repository.save(dataRequest);
	}

	public int getCountSinceDate(Instant date) {
		return repository.getCountSinceDate(date);
	}

	public int getCountWithStatus(Status status) {
		return repository.getCountWithStatus(status);
	}

	public void sendDataRecievedEmail(EventDataRequest updated, EventStatusDTO status) {
		if (status == EventStatusDTO.DATA_RECEIVED) {
			eventEmailProvider.sendDataRecievedEmailAsynchroniously(updated);
		}
	}
}
