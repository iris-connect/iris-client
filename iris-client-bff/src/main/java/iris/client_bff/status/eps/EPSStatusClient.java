package iris.client_bff.status.eps;

import iris.client_bff.config.RPCClientConfig;
import iris.client_bff.status.AppStatusException;
import iris.client_bff.status.eps.dto.Directory;
import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.eps.dto.Ping;
import lombok.AllArgsConstructor;

import java.net.ConnectException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Service
@AllArgsConstructor
public class EPSStatusClient {

	private final JsonRpcHttpClient epsRpcClient;
	private final RPCClientConfig rpcClientConfig;

	public List<DirectoryEntry> getAvailableApps() {

		var methodName = rpcClientConfig.getOwnEndpoint() + "._directory";

		try {
			return epsRpcClient.invoke(methodName, null, Directory.class).getEntries().stream()
					.filter(directoryEntry -> directoryEntry.getGroups().contains("checkin-apps")).collect(Collectors.toList());
		} catch (Throwable t) {
			throw new AppStatusException(t.getMessage(), false);
		}
	}

	public Ping checkApp(String epsEndpoint) {

		var methodName = epsEndpoint + "._ping";

		try {
			return epsRpcClient.invoke(methodName, null, Ping.class);
		} catch (ConnectException e) {
			throw new AppStatusException(e.getMessage(), true);
		} catch (Throwable t) {
			throw new AppStatusException(t.getMessage(), false);
		}
	}
}
