package iris.client_bff.core.log;

import org.apache.commons.lang3.Range;

import java.util.stream.IntStream;

public class LogHelper {

    public static final String DATA_REQ = "DATA REQUEST";
    public static final String CASE_DATA_REQUEST = buildLogMsg(DATA_REQ, "Case data request");
    public static final String EVENT_DATA_REQUEST = buildLogMsg(DATA_REQ, "Event data request");
    public static final String LOCATION_DETAILS = buildLogMsg(DATA_REQ, "Get location details");
    public static final String LOCATION_SEARCH = buildLogMsg(DATA_REQ, "Location search started");

    public static String buildLogMsg(String eventId, String detail) {
        return String.format("%s: %s", eventId, detail);
    }

    /**
     * Extends unobfuscated range with 3 to both sides.
     */
    public static String obfuscateOutsiteExtRange(String s, Range<Integer> range) {

        if (s == null) {
            return null;
        }

        var extRange = range != null
                ? Range.between(range.getMinimum() - 3, range.getMaximum() + 3)
                : Range.is(-1);

        return IntStream.range(0, s.length())
                .map(i -> extRange.contains(i) ? s.codePointAt(i) : '*')
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }
}