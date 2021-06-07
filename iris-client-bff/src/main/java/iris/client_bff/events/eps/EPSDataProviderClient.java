package iris.client_bff.events.eps;

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

	private final JsonRpcHttpClient epsRpcClient;

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
			epsRpcClient.invoke(methodName, payload);
			log.debug("requestGuestListData done: method {}; request {};", methodName, requestId);

		} catch (Throwable t) {
			log.error("requestGuestListData error: method {}; request {}; error {}", methodName, requestId, t);

			throw new IRISDataRequestException(t);
		}
	}

	@Override
	public void abortGuestListDataRequest(EventDataRequest request) throws IRISDataRequestException {
		var requestId = request.getId().toString();
		var methodName = request.getLocation().getProviderId() + ".abortDataRequest";

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
			log.trace("abortGuestListDataRequest start: method {}; request {}", methodName, requestId);
			epsRpcClient.invoke(methodName, payload);
			log.debug("abortGuestListDataRequest done: method {}; request {};", methodName, requestId);

		} catch (Throwable t) {
			log.error("abortGuestListDataRequest error: method {}; request {}; error {}", methodName, requestId, t);
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
