package iris.client_bff.status.eps;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.config.RPCClientConfig;
import iris.client_bff.status.eps.dto.*;
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

    @Override
    public List<DirectoryEntry> getAvailableApps() throws RuntimeException {
        var methodName = rpcClientConfig.getOwnEndpoint()+"._directory";
        try {
            return epsRpcClient.invoke(methodName, null, Directory.class).getEntries().stream().filter(directoryEntry -> directoryEntry.getGroups().contains("checkin-apps")).collect(Collectors.toList());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public Ping checkApp(String epsEndpoint) throws RuntimeException {
        var methodName = epsEndpoint+"._ping";
        try {
            return epsRpcClient.invoke(methodName, null, Ping.class);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
