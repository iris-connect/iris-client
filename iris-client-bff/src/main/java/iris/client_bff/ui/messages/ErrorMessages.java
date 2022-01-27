package iris.client_bff.ui.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {

	public static final String REQUEST_TOO_LARGE = "Request content length exceeded limit!";
	public static final String EVENT_DATA_REQUEST_CREATION = "Fehler: Ereignisnachverfolgung konnte nicht angelegt werden.";
	public static final String LOCATION_SEARCH = "Fehler: Ereignisortsuche fehlgeschlagen.";
	public static final String CASE_DATA_REQUEST_CREATION = "Indexfall-Datenanforderung konnte nicht angelegt werden.";
	public static final String INVALID_INPUT = "Eingabedaten sind ungültig";
	public static final String INVALID_INPUT_STRING = "INVALID";
	public static final String MISSING_REQUIRED_INPUT = "Die übergebenen Daten dürfen nicht leer sein";
	public static final String PW_ERROR_MESSAGE = "The specified password does not follow the password policy (≥ 8 characters, no spaces, lowercase and uppercase letters, two numbers or special character [_-#()@§!$]).";
	public static final String IRIS_MESSAGE_SUBMISSION = "Fehler: Nachricht konnte nicht gesendet werden";
	public static final String INVALID_IRIS_MESSAGE_ID = "Die Nachrichten ID ist ungültig.";
	public static final String INVALID_IRIS_MESSAGE_RECIPIENT = "Der Empfänger der Nachricht ist ungültig.";
	public static final String MISSING_IRIS_MESSAGE_HD_CONTACTS = "Die Gesundheitsamt-Kontakte konnten nicht geladen werden.";
	public static final String INVALID_IRIS_MESSAGE_FOLDER_ID = "Die ID des Nachrichten-Ordners ist ungültig.";
	public static final String INVALID_IRIS_MESSAGE_FOLDER = "Der Nachrichten-Ordner ist ungültig.";
	public static final String INVALID_IRIS_MESSAGE_FILE_ID = "Die ID des Nachrichten Anhangs ist ungültig.";
	public static final String INVALID_IRIS_MESSAGE_DATA_ID = "Die ID des Nachrichten Daten-Anhangs ist ungültig.";
	public static final String INVALID_IRIS_MESSAGE_FILE = "Der Nachrichten Anhang ist ungültig.";
	public static final String INVALID_IRIS_MESSAGE_DATA = "Der Nachrichten Daten-Anhang ist ungültig.";
	public static final String INVALID_FILE_TYPE = "Der Dateityp ist ungültig.";
	public static final String MAX_UPLOAD_FILE_SIZE = "Die Maximalgröße für Dateien von {{max}} wurde überschritten.";

}
