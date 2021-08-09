package iris.client_bff.events;

import iris.client_bff.core.log.LogHelper;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.model.Guest;
import iris.client_bff.events.model.GuestListDataProvider;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EventDataSubmissionService {

	private static final String ERR_SUBM_NOT_ALLOWED = "Error: Submission not allowed for case ";

	private final ModelMapper mapper;

	private final EventDataSubmissionRepository submissions;

	private final EventDataRequestService requestService;
	private final EventEmailProvider eventEmailProvider;
	private final ProxyServiceClient proxyClient;

	public String findRequestAndSaveGuestList(UUID dataAuthorizationToken, GuestList guestList) {
		return requestService.findById(dataAuthorizationToken).map(dataRequest -> {

			switch (dataRequest.getStatus()) {
				case ABORTED: {
					logSubmissionStatus("aborted", dataAuthorizationToken, dataRequest.getId());
					return ERR_SUBM_NOT_ALLOWED + dataAuthorizationToken.toString() + ". Request was aborted.";
				}
				case CLOSED: {
					logSubmissionStatus("closed", dataAuthorizationToken, dataRequest.getId());
					return ERR_SUBM_NOT_ALLOWED + dataAuthorizationToken.toString() + ". Request already closed.";
				}
				case DATA_RECEIVED: {
					logSubmissionStatus("received", dataAuthorizationToken, dataRequest.getId());
					return ERR_SUBM_NOT_ALLOWED + dataAuthorizationToken.toString() + ". Data already received.";
				}
				case DATA_REQUESTED:
					logSubmissionStatus("requested", dataAuthorizationToken, dataRequest.getId());
					break;
				default: {
					logSubmissionStatus(dataRequest.getStatus().toString(), dataAuthorizationToken, dataRequest.getId());
					return ERR_SUBM_NOT_ALLOWED + dataAuthorizationToken.toString() + ". Data not requested.";
				}
			}

			save(dataRequest, guestList);
			eventEmailProvider.sendDataReceivedEmailAsynchronously(dataRequest);

			try {
				proxyClient.abortAnnouncement(dataRequest.getAnnouncementToken());
			} catch (IRISAnnouncementException e) {
				log.error("Abort announcement failed for {}", LogHelper.obfuscateAtStart8(dataRequest.getAnnouncementToken()), e);
			}
			return "OK";

		}).orElseGet(() -> {
			log.error("Data submission for unknown data request occurred: {}", LogHelper.obfuscateAtStart8(dataAuthorizationToken.toString()));

			return "Unknown dataAuthorizationToken: " + dataAuthorizationToken;
		});
	}

	private void logSubmissionStatus(String status, UUID dataAuthorizationToken, EventDataRequest.DataRequestIdentifier dataRequestId) {
		log.trace("Submission {} for {} event {}", LogHelper.obfuscateAtStart8(dataAuthorizationToken.toString()),
				status,	LogHelper.obfuscateAtStart8(dataRequestId.toString()));
	}

	private void save(EventDataRequest dataRequest, GuestList guestList) {
		var guests = guestList.getGuests().stream().map(it -> mapper.map(it, Guest.class)).collect(Collectors.toSet());

		var dataProvider = mapper.map(guestList.getDataProvider(), GuestListDataProvider.class);

		var submission = new EventDataSubmission(
			dataRequest,
			guests,
			dataProvider,
			guestList.getAdditionalInformation(),
			guestList.getStartDate(),
			guestList.getEndDate());

		guests.forEach(it -> it.setSubmission(submission));

		submissions.save(submission);

		dataRequest.setStatus(Status.DATA_RECEIVED);

		requestService.save(dataRequest);
	}

}
