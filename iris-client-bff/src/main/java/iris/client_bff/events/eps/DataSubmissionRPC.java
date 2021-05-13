package iris.client_bff.events.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import iris.client_bff.events.web.dto.GuestList;
import java.util.UUID;

@JsonRpcService("/data-submission-rpc")
public interface DataSubmissionRPC {

  String submitGuestList(
      @JsonRpcParam(value = "requestId") UUID providerId,
      @JsonRpcParam(value = "guestList") GuestList guestList);

}