package iris.client_bff.events.eps;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class EPSDataProviderClient implements DataProviderClient {

	private final JsonRpcHttpClient rpcClient;

	public void requestGuestListData(EventDataRequest request) throws IRISDataRequestException {

		var requestId = request.getId().toString();
		var methodName = request.getLocation().getProviderId() + ".createDataRequest";

		var payload = Map.of(
				"dataRequest", SubmitPayload.builder()
						.start(request.getRequestStart())
						.end(request.getRequestEnd())
						.dataAuthorizationToken(requestId)
						.proxyEndpoint(request.getAnnouncementToken())
						.locationId(request.getLocation().getLocationId())
						.requestDetails(request.getRequestDetails())
						.build());

		try {
			log.trace("requestGuestListData start: method {}; request {}", methodName, requestId);
			rpcClient.invoke(methodName, payload);
			log.debug("requestGuestListData done: method {}; request {};", methodName, requestId);

		} catch (Throwable t) {
			log.error("requestGuestListData error: method {}; request {}; error {}", methodName, requestId, t);

			throw new IRISDataRequestException(t);
		}
	}

	@Data
	@Builder
	public static class SubmitPayload {

		String dataAuthorizationToken;

		String proxyEndpoint;

		String requestDetails;

		String locationId;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
		Instant start;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
		Instant end;
	}
}
