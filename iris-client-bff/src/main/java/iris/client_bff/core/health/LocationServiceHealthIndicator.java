package iris.client_bff.core.health;

import iris.client_bff.config.BackendServiceProperties;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

/**
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
class LocationServiceHealthIndicator implements HealthIndicator {

	private final BackendServiceProperties config;
	private final JsonRpcHttpClient epsRpcClient;

	@Override
	public Health health() {

		try {

			var methodName = config.getEndpoint() + ".getHealth";
			var status = epsRpcClient.invoke(methodName, null, Status.class);

			return Health.status(status).build();

		} catch (Throwable e) {

			return Health.down().withException(e).build();
		}
	}
}
