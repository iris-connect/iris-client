package iris.client_bff.core.log;

public class LogEvent {

    public static final String DATA_REQ = "DATA REQUEST: ";

    public static String buildLogMsg(String eventId, String detail) {
        return String.format("%s: %s", eventId, detail);
    }
}