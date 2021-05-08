package iris.client_bff.data_submission.eps;

import iris.client_bff.data_request.events.EventDataRequestService;
import iris.client_bff.data_request.events.web.dto.GuestList;
import iris.client_bff.data_submission.EventDataSubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class DataSubmissionRPCImpl implements DataSubmissionRPC {

	private final EventDataRequestService requestService;
	private final EventDataSubmissionService dataSubmissionService;

	public String submitGuestList(UUID requestId, GuestList guestList) {
		log.trace("Start submission {}", requestId);

		return requestService.findById(requestId).map(dataRequest -> {

			dataSubmissionService.save(dataRequest, guestList);

			log.trace("Done submission {}", requestId);

			return "OK";

		}).orElseGet(() -> {

			// TODO sufficient?

			log.error("Data submission for unknown data request occurred: {}", requestId);

			return "Unknown requestId: " + requestId.toString();
		});
	}
}
