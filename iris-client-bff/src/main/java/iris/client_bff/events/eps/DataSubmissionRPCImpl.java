package iris.client_bff.events.eps;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.events.web.dto.GuestList;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class DataSubmissionRPCImpl implements DataSubmissionRPC {

	private final EventDataRequestService requestService;
	private final EventDataSubmissionService dataSubmissionService;
	private final ProxyServiceClient proxyClient;
	private final DataProviderClient epsDataRequestClient;

	public String submitGuestList(JsonRpcClientDto client, UUID dataAuthorizationToken, GuestList guestList) {
		log.trace("Start submission {}", dataAuthorizationToken);

		// Todo check client.getName() vs. providerId

		return requestService.findById(dataAuthorizationToken).map(dataRequest -> {

			if (dataRequest.getStatus().equals(EventDataRequest.Status.ABORTED)) {
				log.trace("Submission {} for aborted event {}", dataAuthorizationToken, dataRequest.getId());
				return "Error: Submission not allowed for " + dataAuthorizationToken.toString() + ". Request was aborted.";
			}

			try {
				dataSubmissionService.save(dataRequest, guestList);
				proxyClient.abortAnnouncement(dataRequest.getAnnouncementToken());
				epsDataRequestClient.abortGuestListDataRequest(dataRequest);
				log.trace("Submission of data for {} is complete and proxy ancouncement has been closed", dataAuthorizationToken);
			} catch (IRISAnnouncementException | IRISDataRequestException e) {
				// Todo: Do I also need to remove the submission ? Possibly remove the saved data again
				requestService.save(dataRequest);
				e.printStackTrace();
				log.trace("Submission of data or closing of the proxy announcement failed for {}", dataRequest.getAnnouncementToken());
			}
			return "OK";

		}).orElseGet(() -> {

			// TODO sufficient?
			// probably throw exception

			log.error("Data submission for unknown data request occurred: {}", dataAuthorizationToken);

			return "Unknown dataAuthorizationToken: " + dataAuthorizationToken.toString();
		});
	}
}
