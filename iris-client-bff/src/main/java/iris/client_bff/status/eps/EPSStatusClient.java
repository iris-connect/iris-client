package iris.client_bff.status.eps;

import static java.util.stream.Collectors.*;

import iris.client_bff.config.RPCClientConfig;
import iris.client_bff.status.AppInfo;
import iris.client_bff.status.AppStatusException;
import iris.client_bff.status.AppStatusInternalException;
import iris.client_bff.status.Apps;
import iris.client_bff.status.Apps.App;
import iris.client_bff.status.EpsConnectionException;
import lombok.AllArgsConstructor;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.jsonrpc4j.JsonRpcClientException;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Service
@AllArgsConstructor
public class EPSStatusClient {

	private final JsonRpcHttpClient epsRpcClient;
	private final RPCClientConfig rpcClientConfig;

	public Apps getAvailableApps() {

		var methodName = rpcClientConfig.getOwnEndpoint() + "._directory";

		try {

			return epsRpcClient.invoke(methodName, null, Directory.class).entries().stream()
					.filter(directoryEntry -> directoryEntry.groups().contains("checkin-apps"))
					.map(DirectoryEntry::name)
					.map(App::new)
					.collect(collectingAndThen(toList(), Apps::new));

		} catch (JsonRpcClientException e) {
			throw new AppStatusException(e.getMessage());
		} catch (Throwable t) {
			throw new AppStatusInternalException(t.getMessage(), t);
		}
	}

	public AppInfo checkApp(String epsEndpoint) {

		var methodName = epsEndpoint + "._ping";

		try {

			var ping = epsRpcClient.invoke(methodName, null, Ping.class);

			var name = ping.serverInfo != null ? ping.serverInfo.name : null;

			return new AppInfo(name, ping.version);

		} catch (ConnectException | SocketTimeoutException e) { // Connection timeout
			throw new EpsConnectionException(e.getMessage());
		} catch (JsonRpcClientException e) {
			throw new AppStatusException(e.getMessage());
		} catch (Throwable t) {
			throw new AppStatusInternalException(t.getMessage(), t);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	record Directory(@NotNull List<@Valid DirectoryEntry> entries) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	record DirectoryEntry(@NotNull String name, Set<String> groups) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	record Ping(String version, ServerInfo serverInfo) {};

	@JsonIgnoreProperties(ignoreUnknown = true)
	record ServerInfo(String name) {};
}
