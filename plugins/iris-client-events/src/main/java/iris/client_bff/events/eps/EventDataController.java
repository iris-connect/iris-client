package iris.client_bff.events.eps;

import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.web.dto.GuestList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;

@Validated
public interface EventDataController {

	@JsonRpcErrors({
			@JsonRpcError(exception = ResponseStatusException.class, code = -32600)
	})
	String submitGuestList(
			@JsonRpcParam(value = "_client") @Valid JsonRpcClientDto client,
			@JsonRpcParam(value = "dataAuthorizationToken") DataRequestIdentifier dataAuthorizationToken,
			@JsonRpcParam(value = "guestList") @NotNull @Valid GuestList guestList);
}
