package iris.client_bff.events.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import iris.client_bff.events.web.dto.GuestList;

import javax.validation.Valid;
import java.util.UUID;

@JsonRpcService("/data-submission-rpc")
public interface DataSubmissionRPC {

    String submitGuestList(
            @Valid @JsonRpcParam(value = "_client") JsonRpcClientDto client,
            @JsonRpcParam(value = "dataAuthorizationToken") UUID dataAuthorizationToken,
            @JsonRpcParam(value = "guestList") GuestList guestList);

}
