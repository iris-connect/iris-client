package iris.client_bff.core.alert;

import iris.client_bff.config.BackendServiceProperties;
import iris.client_bff.core.alert.AlertDto.AlertDtoBuilder;
import iris.client_bff.core.alert.AlertDto.AlertType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.IJsonRpcClient;

/**
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

	private final BackendServiceProperties config;
	private final IJsonRpcClient epsRpcClient;

	private static final String APP = "iris-bff";
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
	 */
	public void createAlertMessage(String title, String text) {

		log.warn("Alert: {} - {}", title, text);

		sendAlert(List.of(message(title, text)));
	}

	/**
	 * Creates a ticket in the ticket system of the IRIS-Connect project in addition to the message. This should be used
	 * for serious violations and sure abuses.
	 * <p>
	 * The text is also logged locally by the method!
	 * </p>
	 */
	public void createAlertTicketAndMessage(String title, String text) {

		log.warn("Alert: {} - {}", title, text);

		sendAlert(List.of(message(title, text), ticket(title, text)));
	}

	private AlertDto message(String title, String text) {
		return createAlertBuilder(title, text).alertType(AlertType.MESSAGE).build();
	}

	private AlertDto ticket(String title, String text) {
		return createAlertBuilder(title, text).alertType(AlertType.TICKET).build();
	}

	private AlertDtoBuilder createAlertBuilder(String title, String text) {
		return AlertDto.builder()
				.title(title)
				.text(text)
				.sourceApp(APP)
				.appVersion(version);
	}

	private void sendAlert(List<AlertDto> alerts) {
		var methodName = config.getEndpoint() + ".postAlerts";
		try {
			AlertListDto alertListDto = AlertListDto.builder().alertList(alerts).build();
			var result = epsRpcClient.invoke(methodName, alertListDto, String.class);

			if (!"OK".equals(result)) {
				log.error("Alert Service - could not be sent => Result: {}", result);
			}

		} catch (Throwable t) {
			log.error("Alert Service - could not be sent => Exception", t);
		}
	}
}
