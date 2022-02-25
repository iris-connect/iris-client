package iris.client_bff.hd_search.eps;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.config.BackendServiceProperties;
import iris.client_bff.hd_search.HdSearchException;
import iris.client_bff.hd_search.HealthDepartment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class EPSHdSearchClient {

    private final JsonRpcHttpClient epsRpcClient;
    private BackendServiceProperties config;

    public List<HealthDepartment> searchForHd(String search) {

        var methodName = config.getEndpoint() + ".searchForHd";
        Map<String, String> payload  = Map.of("searchKeyword", search);

        try {
            return Arrays.stream(epsRpcClient.invoke(methodName, payload, HealthDepartment[].class)).toList();
        } catch (Throwable t) {
            throw new HdSearchException(methodName, t);
        }
    }
}
