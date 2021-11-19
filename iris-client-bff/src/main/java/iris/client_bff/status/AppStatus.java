package iris.client_bff.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Getter
public enum AppStatus implements MessageSourceResolvable {

	OK(Status.OK, Optional.empty()), //
	CANT_CONNECT_LOCAL_EPS(Status.ERROR, Optional.empty()), //
	UNKNOWN_ERROR(Status.ERROR, Optional.empty()), //
	PERMISSION_DENIED_PING_CLIENT(Status.WARNING, Optional.of("Permission denied for method '_ping' and client")), //
	CERTIFICATE_INVALID_CLIENT(Status.ERROR, Optional.of("certificate is valid for [\\w.-]+, not [\\w.-]+")), //
	CERTIFICATE_INVALID_DATE(Status.ERROR, Optional.of("certificate has expired or is not yet valid")), //
	CONNECTION_CLOSED_BY_REMOTE(Status.ERROR,
			Optional.of("Eine vorhandene Verbindung wurde vom Remotehost geschlossen")), //
	ACCESS_DENIED(Status.ERROR,
			Optional.of("Es konnte keine Verbindung hergestellt werden, da der Zielcomputer die Verbindung verweigerte")), //
	NO_SUCH_HOST(Status.ERROR, Optional.of("no such host")), //
	TIMEOUT(Status.ERROR, Optional.of("Error while dialing dial tcp [\\d.:\\[\\]]+: i/o timeout"));

	private Status status;
	private Optional<Pattern> messagePattern;

	AppStatus(Status status, Optional<String> messagePatternStr) {
		this.status = status;
		messagePattern = messagePatternStr.map(Pattern::compile);
	}

	@Override
	public String[] getCodes() {
		return new String[] { "app.status." + name().toLowerCase(Locale.US) };
	}

	public enum Status {
		OK, WARNING, ERROR;
	}

	@Service
	@AllArgsConstructor
	public static class Resolver {

		public AppStatus getStatusOk() {
			return OK;
		}

		public AppStatus getCantConnectLocalEps() {
			return CANT_CONNECT_LOCAL_EPS;
		}

		public AppStatus getStatusByErrorMessage(String message) {

			for (AppStatus appStatus : AppStatus.values()) {
				if (appStatus.getMessagePattern()
						.map(it -> it.matcher(message))
						.filter(Matcher::find)
						.isPresent()) {

					return appStatus;
				}
			}

			return UNKNOWN_ERROR;
		}
	}
}
