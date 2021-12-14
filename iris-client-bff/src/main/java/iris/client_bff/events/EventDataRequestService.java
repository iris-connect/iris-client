package iris.client_bff.events;

import static iris.client_bff.search_client.eps.LocationMapper.*;
import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.core.log.LogHelper;
import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.eps.DataProviderClient;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.web.dto.DataRequestClient;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventDataRequestService {

	private static final String[] FIELDS = { "refId", "name", "location.location_name", "location.contactOfficialName",
			"location.contactAddressStreet", "location.contactAddressCity", "location.contactAddressZip" };

	private final EventDataRequestRepository repository;
	private final HibernateSearcher searcher;
	private final SearchClient searchClient;
	private final ProxyServiceClient proxyClient;
	private final DataProviderClient epsDataRequestClient;

	public Page<EventDataRequest> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Page<EventDataRequest> findByStatus(Status status, Pageable pageable) {
		return repository.findByStatus(status, pageable);
	}

	public Page<EventDataRequest> search(Status status, String searchString, Pageable pageable) {

		var result = searcher.search(
				searchString,
				pageable,
				FIELDS,
				it -> status != null ? it.must(f2 -> f2.match().field("status").matching(status)) : it,
				EventDataRequest.class);

		return new PageImpl<>(result.hits(), pageable, result.total().hitCount());
	}

	public Page<EventDataRequest> search(String search, Pageable pageable) {
		return search(null, search, pageable);
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
			location = map(searchClient.findByProviderIdAndLocationId(providerId, locationId));
		} catch (IRISSearchException e) {
			log.error("Location {} with provider {} could not be obtained: {}", locationId, providerId, e.getMessage());

			throw new IRISDataRequestException(e);
		}

		String announcementToken;
		try {
			announcementToken = proxyClient.announce();
		} catch (IRISAnnouncementException e) {
			log.error("Announcement failed: ", e);

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
			log.info(LogHelper.EVENT_DATA_REQUEST);
			epsDataRequestClient.requestGuestListData(dataRequest);
		} catch (IRISDataRequestException e) {

			repository.delete(dataRequest);

			throw e;
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
			if (dataRequest.getStatus() != status) {
				dataRequest.setStatus(status);

				try {
					proxyClient.abortAnnouncement(dataRequest.getAnnouncementToken());
					epsDataRequestClient.abortGuestListDataRequest(dataRequest);
				} catch (IRISAnnouncementException | IRISDataRequestException e) {
					log.error("Abort announcement for token {} failed", dataRequest.getAnnouncementToken(), e);
				}
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
}
