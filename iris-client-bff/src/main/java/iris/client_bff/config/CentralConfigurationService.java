package iris.client_bff.config;

import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.core.alert.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcClientException;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class CentralConfigurationService {

	private static final Duration CACHING_TIME = Duration.ofSeconds(5);

	private final BackendServiceProperties bsProperties;
	private final JsonRpcHttpClient epsRpcClient;
	private final ProxyServiceProperties proxyProperties;
	private final Environment env;
	private final AlertService alerts;

	private Configuration config;
	private Instant lastFetch = Instant.MIN;
	private boolean ignoreConnectionErrors;

	public String getAbbreviation() {
		return fetchConfig().abbreviation;
	}

	public String getCatSalt() {
		return fetchConfig().catSalt;
	}

	public String getDatSalt() {
		return fetchConfig().datSalt;
	}

	public int getCatLength() {
		return fetchConfig().catLength();
	}

	public int getDatLength() {
		return fetchConfig().datLength();
	}

	private String getProxyUrl() {
		return fetchConfig().proxyUrl;
	}

	@PostConstruct
	void checkValues() {

		if (Arrays.stream(env.getActiveProfiles())
				.anyMatch(it -> equalsAny(it, "local", "dev_env", "inttest"))) {

			// Fallback for local dev and integration tests without a running backend system. However, an existing OS can also
			// overwrite the default during development.
			config = new Configuration("000", "proxy.dev.test-gesundheitsamt.de", RandomStringUtils.randomAlphanumeric(32),
					RandomStringUtils.randomAlphanumeric(32), 40, 40);
			ignoreConnectionErrors = true;
		}

		try {

			var proxyUrl = getProxyUrl();

			if (!StringUtils.equals(proxyUrl, proxyProperties.getTargetSubdomain())) {

				alerts.createAlertTicketAndMessage("Configuration mismatch of proxy target subdomain", String.format(
						"""
								The proxy target subdomain is configured different in this client (via environment variable) and in backend service (by the IRIS team)!

								Client: %s
								BS: %s
								""",
						proxyProperties.getTargetSubdomain(), proxyUrl));

				throw new CentralConfigurationException(
						"The proxy target subdomain is configured different in this client (via environment variable) and in backend service (by the IRIS team)!");
			}
		} catch (MissingCentralConfigurationException e) {

			// We ignore this exception at startup time so that the clients in the health departments continue to start even
			// if the central configuration is not yet complete. The functions for index cases are currently not yet enabled.
			log.warn(e.getLocalizedMessage());
		}
	}

	private Configuration fetchConfig() {

		// Use the cache if the CACHING_TIME has not expired yet.
		if (lastFetch.plus(CACHING_TIME).isAfter(Instant.now())) {
			return config;
		}

		var methodName = bsProperties.getEndpoint() + ".getHdConfiguration";

		try {

			config = epsRpcClient.invoke(methodName, null, Configuration.class);
			lastFetch = Instant.now();

			return config;

		} catch (JsonRpcClientException e) {

			if (e.getCode() == -1111) {
				throw new MissingCentralConfigurationException(e.getLocalizedMessage());
			}

			return handleException(e);

		} catch (Throwable e) {
			return handleException(e);
		}
	}

	private Configuration handleException(Throwable e) {

		if (ignoreConnectionErrors) {

			log.debug("Ignore connection error of central connection service.");

			return config;
		}

		alerts.createAlertMessage("Fetch central configuration error", String.format(
				"Can't fetch the central configuration from backend service. (Exception: %s)",
				e.getClass().getCanonicalName()));

		throw new CentralConfigurationException("Can't fetch the central configuration from backend service.", e);
	}

	private record Configuration(String abbreviation, String proxyUrl, String catSalt, String datSalt, int catLength,
			int datLength) {}

	class CentralConfigurationException extends RuntimeException {

		private static final long serialVersionUID = -1022288204463580006L;

		CentralConfigurationException(String message) {
			super(message);
		}

		CentralConfigurationException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	class MissingCentralConfigurationException extends CentralConfigurationException {

		private static final long serialVersionUID = 918855771353238027L;

		MissingCentralConfigurationException(String message) {
			super(message);
		}
	}
}
