package iris.client_bff.core.log;

public class LogHelper {

    public static final String DATA_REQ = "DATA REQUEST";

    public static String buildLogMsg(String eventId, String detail) {
        return String.format("%s: %s", eventId, detail);
    }

    public static String caseDataRequest() {
        return String.format("%s: %s", DATA_REQ, "Case data request");
    }

    public static String eventDataRequest() {
        return String.format("%s: %s", DATA_REQ, "Event data request");
    }

    public static String locationDetails() {
        return String.format("%s: %s", DATA_REQ, "Get location details");
    }

    public static String locationSearch() {
        return String.format("%s: %s", DATA_REQ, "Location search started");
    }
}