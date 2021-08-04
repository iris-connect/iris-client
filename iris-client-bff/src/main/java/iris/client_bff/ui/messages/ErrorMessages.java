package iris.client_bff.ui.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {

	public static final String EVENT_DATA_REQUEST_CREATION = "Fehler: Ereignisnachverfolgung konnte nicht angelegt werden.";
	public static final String LOCATION_SEARCH = "Fehler: Ereignisortsuche fehlgeschlagen.";
	public static final String CASE_DATA_REQUEST_CREATION = "Indexfall-Datenanforderung konnte nicht angelegt werden.";
	public static final String INVALID_INPUT = "Eingabedaten sind ungültig";
	public static final String INVALID_INPUT_STRING = "INVALID";
	public static final String MISSING_REQUIRED_INPUT = "Die übergebenen Daten dürfen nicht leer sein";
}
