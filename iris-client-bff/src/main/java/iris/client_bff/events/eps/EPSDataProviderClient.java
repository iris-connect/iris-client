package iris.client_bff.events.eps;

import iris.client_bff.config.IrisClientProperties;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Slf4j
@Service
@AllArgsConstructor
public class EPSDataProviderClient implements DataProviderClient {

	private final JsonRpcHttpClient rpcClient;

	private final IrisClientProperties clientProperties;

	public void requestGuestListData(EventDataRequest request) throws IRISDataRequestException {

		var requestId = request.getId().toString();
		var methodName = request.getLocation().getProviderId() + ".createDataRequest";

		var payload = Map.of(
				"dataRequest", SubmitPayload.builder()
						.start(request.getRequestStart())
						.end(request.getRequestEnd())
						.hdEndpoint("hd-1")
						.dataAuthorizationToken(requestId)
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
		String hdEndpoint;
		String dataAuthorizationToken;

		String requestDetails;

		String locationId;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
		Instant start;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
		Instant end;
	}
}
