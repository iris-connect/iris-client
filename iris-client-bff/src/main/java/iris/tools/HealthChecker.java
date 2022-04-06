package iris.tools;

import lombok.SneakyThrows;

import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.client.HttpServerErrorException.ServiceUnavailable;
import org.springframework.web.client.RestTemplate;

/**
 * This small application is intended to check the health of a Spring application via its actuator response on
 * <code>localhost</code> when that application is running in a container. This is especially useful for containers
 * built with Buildpacks (CNB, <code>spring-boot-maven-plugin</code>), since they do not contain curl or wget.
 * <p />
 * It is inspired by: <a>https://mflash.dev/blog/2021/03/01/java-based-health-check-for-docker/</a>
 * <p />
 * Use it with:
 *
 * <pre>
 * {@code
 * healthcheck:
 *   test: "/cnb/lifecycle/launcher java -cp /workspace/BOOT-INF/classes:/workspace/BOOT-INF/lib/* iris.tools.HealthChecker [-d] || exit 1"
 *   interval: 20s
 *   timeout: 3s
 *   retries: 2
 *   start_period: 10s
 * }
 * </pre>
 *
 * @author Jens Kutzsche
 */
public class HealthChecker {

	/**
	 * URL of the Spring health actuator.
	 */
	private static final String URL = "http://localhost:{port}/actuator/health";

	@SneakyThrows(IOException.class)
	public static void main(String[] args) {

		var properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
		var port = properties.getProperty("server.port", "8080");
		port = System.getProperty("server.port", port);
		port = SystemUtils.getEnvironmentVariable("SERVER_PORT", port);

		var rest = new RestTemplate();

		if (args.length > 0 && "-d".equals(args[0])) {
			try {

				var response = rest.getForObject(URL, String.class, port);
				System.out.println(response);

			} catch (ServiceUnavailable e) {

				e.printStackTrace();
				System.err.println(e.getResponseBodyAsString());
			}
		}

		var healthy = false;
		try {

			var response = rest.getForEntity(URL, HealthCheck.class, port);

			if (response.getStatusCode().is2xxSuccessful()
					&& response.hasBody()
					&& Status.UP.equals(response.getBody().status)) {

				healthy = true;
			}

		} catch (Exception e) {}

		if (!healthy) {
			System.err.println("Health check failed");
			System.exit(1);
		}

		System.out.println("Health check successful");
	}

	record HealthCheck(Status status) {}
}
