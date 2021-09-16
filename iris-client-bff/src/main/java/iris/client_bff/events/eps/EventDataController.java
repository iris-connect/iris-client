package iris.client_bff.events.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import iris.client_bff.events.web.dto.GuestList;

import javax.validation.Valid;
import java.util.UUID;

public interface EventDataController {
    String submitGuestList(
            @Valid @JsonRpcParam(value = "_client") JsonRpcClientDto client,
            @JsonRpcParam(value = "dataAuthorizationToken") UUID dataAuthorizationToken,
            @JsonRpcParam(value = "guestList") GuestList guestList);
}
