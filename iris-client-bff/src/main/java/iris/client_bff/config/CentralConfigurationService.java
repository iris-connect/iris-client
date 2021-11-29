package iris.client_bff.config;

import static org.apache.commons.lang3.StringUtils.*;

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

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class CentralConfigurationService {

	private static final Duration WAITING = Duration.ofSeconds(5);

	private final BackendServiceProperties bsProperties;
	private final JsonRpcHttpClient epsRpcClient;
	private final ProxyServiceProperties proxyProperties;
	private final Environment env;

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

			config = new Configuration("000", "proxy.test-gesundheitsamt.de", RandomStringUtils.randomAlphanumeric(32),
					RandomStringUtils.randomAlphanumeric(32), 40, 40);
			ignoreConnectionErrors = true;

		}

		if (!StringUtils.equals(proxyProperties.getTargetSubdomain(), getProxyUrl())) {
			throw new CentralConfigurationException(
					"The proxy target subdomain is configured differend in this client (via environment variable) and in backend service (by the IRIS team)!");
		}
	}

	private Configuration fetchConfig() {

		if (lastFetch.plus(WAITING).isAfter(Instant.now())) {
			return config;
		}

		var methodName = bsProperties.getEndpoint() + ".getHdConfiguration";

		try {

			config = epsRpcClient.invoke(methodName, null, Configuration.class);
			lastFetch = Instant.now();

			return config;

		} catch (Throwable e) {

			if (ignoreConnectionErrors) {

				log.debug("Ignore connection error of central connection service.");

				return config;

			} else {
				throw new CentralConfigurationException("Can't fetch the central configuration from backend service!", e);
			}
		}
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
}
