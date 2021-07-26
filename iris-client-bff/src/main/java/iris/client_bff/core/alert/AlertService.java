package iris.client_bff.core.alert;

import iris.client_bff.config.BackendServiceProperties;
import iris.client_bff.core.alert.AlertDto.AlertType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

/**
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

	private final BackendServiceProperties config;
	private final JsonRpcHttpClient epsRpcClient;

	private String version = "";
	{
		try {
			var properties = PropertiesLoaderUtils.loadAllProperties("git.properties");
			version = properties.getProperty("git.build.version", "-") + " ("
					+ properties.getProperty("git.commit.id.abbrev", "-")
					+ ")";
		} catch (IOException e) {
			log.error("Can't load git properties", e);
		}
	}

	/**
	 * Creates a message in the communication system of the IRIS-Connect project. This should be used for less serious
	 * violations and suspected abuse.
	 * <p>
	 * The text is also logged locally by the method!
	 * </p>
	 * 
	 * @param text
	 */
	public void createAlertMessage(String text) {

		log.error("Alert: {}", text);

		sendAlert(List.of(message(text)));
	}

	/**
	 * Creates a ticket in the ticket system of the IRIS-Connect project in addition to the message. This should be used
	 * for serious violations and sure abuses.
	 * <p>
	 * The text is also logged locally by the method!
	 * </p>
	 * 
	 * @param text
	 */
	public void createAlertTicketAndMessage(String text) {

		log.error("Alert: {}", text);

		sendAlert(List.of(message(text), ticket(text)));
	}

	private AlertDto message(String text) {
		return AlertDto.builder().text(text).version(version).alertType(AlertType.MESSAGE).build();
	}

	private AlertDto ticket(String text) {
		return AlertDto.builder().text(text).version(version).alertType(AlertType.TICKET).build();
	}

	private void sendAlert(List<AlertDto> alerts) {
		var methodName = config.getEndpoint() + ".postAlerts";
		try {

			var result = epsRpcClient.invoke(methodName, alerts, String.class);

			if (!"OK".equals(result)) {
				log.error("Alert - could not be sent => Result: {}", result);
			}

		} catch (Throwable t) {
			log.error("Alert - could not be sent => Exception", t);
		}
	}
}
