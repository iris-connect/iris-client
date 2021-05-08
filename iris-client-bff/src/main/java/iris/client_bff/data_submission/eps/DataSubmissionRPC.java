package iris.client_bff.data_submission.eps;

import iris.client_bff.data_request.events.web.dto.GuestList;

import java.util.UUID;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/data-submission-rpc")
public interface DataSubmissionRPC {

	String submitGuestList(
			@JsonRpcParam(value = "requestId") UUID providerId,
			@JsonRpcParam(value = "guestList") GuestList guestList);

}
