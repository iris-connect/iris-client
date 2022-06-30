package iris.client_bff.hd_search.eps;

import iris.client_bff.config.BackendServiceProperties;
import iris.client_bff.hd_search.HdSearchException;
import iris.client_bff.hd_search.HealthDepartment;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Service
@AllArgsConstructor
public class EPSHdSearchClient {

	private final JsonRpcHttpClient epsRpcClient;
	private BackendServiceProperties config;

	public List<HealthDepartment> searchForHd(String search) {

		var methodName = config.getEndpoint() + ".searchForHd";
		Map<String, String> payload = Map.of("searchKeyword", search, "withDetails", "false",
				"alsoNotConnectedHds", "false");

		try {
			return Arrays.asList(epsRpcClient.invoke(methodName, payload, HealthDepartment[].class));
		} catch (Throwable t) {
			throw new HdSearchException(methodName, t);
		}
	}

	public List<HealthDepartment> getAllHds() {

		var methodName = config.getEndpoint() + ".getAllHds";
		var payload = Map.of("withDetails", false, "alsoNotConnectedHds", false);

		try {
			return Arrays.asList(epsRpcClient.invoke(methodName, payload, HealthDepartment[].class));
		} catch (Throwable t) {
			throw new HdSearchException(methodName, t);
		}
	}
}
