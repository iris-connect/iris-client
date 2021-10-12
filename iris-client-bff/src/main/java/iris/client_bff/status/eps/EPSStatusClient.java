package iris.client_bff.status.eps;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.config.BackendServiceProperties;
import iris.client_bff.config.RPCClientConfig;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.status.StatusClient;
import iris.client_bff.status.eps.dto.Directory;
import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.eps.dto.Ping;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class EPSStatusClient implements StatusClient {

    private final JsonRpcHttpClient epsRpcClient;
    private final RPCClientConfig rpcClientConfig;
    private BackendServiceProperties config;

    @Override
    public String getAppStati() {
        var methodName = rpcClientConfig.getOwnEndpoint()+"._directory";
        try {
            return epsRpcClient.invoke(methodName, null, Directory.class).getEntries().stream().filter(directoryEntry -> directoryEntry.getGroups().contains("checkin-apps")).collect(Collectors.toList()).toString();
        } catch (Throwable t) {
            throw new IRISSearchException(methodName, t);
        }
    }

    @Override
    public List<DirectoryEntry> getAvailableApps() {
        var methodName = rpcClientConfig.getOwnEndpoint()+"._directory";
        try {
            return epsRpcClient.invoke(methodName, null, Directory.class).getEntries().stream().filter(directoryEntry -> directoryEntry.getGroups().contains("checkin-apps")).collect(Collectors.toList());
        } catch (Throwable t) {
            throw new IRISSearchException(methodName, t);
        }
    }

    public Ping queryEPSStatus(String epsEndpoint) {
        var methodName = epsEndpoint+"._ping";
        try {
            return epsRpcClient.invoke(methodName, null, Ping.class);
        } catch (Throwable t) {
            log.debug("Ping "+epsEndpoint+" failed with error: "+t.getMessage());
            return null;
        }
    }
}
