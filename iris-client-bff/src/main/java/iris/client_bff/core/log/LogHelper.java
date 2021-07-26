package iris.client_bff.core.log;

public class LogHelper {

    public static final String DATA_REQ = "DATA REQUEST";
    public static final String CASE_DATA_REQUEST = buildLogMsg(DATA_REQ, "Case data request");
    public static final String EVENT_DATA_REQUEST = buildLogMsg(DATA_REQ, "Event data request");
    public static final String LOCATION_DETAILS = buildLogMsg(DATA_REQ, "Get location details");
    public static final String LOCATION_SEARCH = buildLogMsg(DATA_REQ, "Location search started");

    public static String buildLogMsg(String eventId, String detail) {
        return String.format("%s: %s", eventId, detail);
    }
}