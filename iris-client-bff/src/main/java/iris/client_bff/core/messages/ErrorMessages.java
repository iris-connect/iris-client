package iris.client_bff.core.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {

	public static final String REQUEST_TOO_LARGE = "Request content length exceeded limit!";
	public static final String EVENT_DATA_REQUEST_CREATION = "Fehler: Ereignisnachverfolgung konnte nicht angelegt werden.";
	public static final String LOCATION_SEARCH = "Fehler: Ereignisortsuche fehlgeschlagen.";
	public static final String CASE_DATA_REQUEST_CREATION = "Indexfall-Datenanforderung konnte nicht angelegt werden.";
	public static final String INVALID_INPUT = "Eingabedaten sind ungültig";
	public static final String USER_NOT_FOUND = "User: %s, not found";
}
