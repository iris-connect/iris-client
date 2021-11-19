package iris.client_bff.status.eps;

import static java.util.stream.Collectors.*;

import iris.client_bff.config.RPCClientConfig;
import iris.client_bff.status.AppInfo;
import iris.client_bff.status.AppStatusException;
import iris.client_bff.status.Apps;
import iris.client_bff.status.Apps.App;
import lombok.AllArgsConstructor;

import java.net.ConnectException;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

		} catch (Throwable t) {
			throw new AppStatusException(t.getMessage(), false);
		}
	}

	public AppInfo checkApp(String epsEndpoint) {

		var methodName = epsEndpoint + "._ping";

		try {

			var ping = epsRpcClient.invoke(methodName, null, Ping.class);

			return new AppInfo(ping.serverInfo.name, ping.version);

		} catch (ConnectException e) {
			throw new AppStatusException(e.getMessage(), true);
		} catch (Throwable t) {
			throw new AppStatusException(t.getMessage(), false);
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
