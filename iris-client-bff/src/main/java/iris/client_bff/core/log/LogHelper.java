package iris.client_bff.core.log;

import java.util.stream.IntStream;

import org.apache.commons.lang3.Range;

public class LogHelper {

    public static final String DATA_REQ = "DATA REQUEST";
    public static final String CASE_DATA_REQUEST = buildLogMsg(DATA_REQ, "Case data request");
    public static final String EVENT_DATA_REQUEST = buildLogMsg(DATA_REQ, "Event data request");
    public static final String LOCATION_DETAILS = buildLogMsg(DATA_REQ, "Get location details");
    public static final String LOCATION_SEARCH = buildLogMsg(DATA_REQ, "Location search started");

    private LogHelper() {}
    
    public static String buildLogMsg(String eventId, String detail) {
        return String.format("%s: %s", eventId, detail);
    }

    /**
     * Obfuscates the string, but excludes the given range plus 3 characters at its begin and end from obfuscation.
     */
    public static String obfuscateWithExcludedRange(String s, Range<Integer> range) {

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

    public static String obfuscateAtStart8(String s) {
        return obfuscate(s, false, 8);
    }

    public static String obfuscateAtStart16(String s) {
        return obfuscate(s, false, 16);
    }

    public static String obfuscateAtStart20(String s) {
        return obfuscate(s, false, 16);
    }

    public static String obfuscate(String s, boolean fromEnd, int obfuscateAmount) {
        if(s == null)
            return null;

        String regex = ".{0,"+obfuscateAmount+"}";
        if(fromEnd)
            regex += "$";
        return s.replaceFirst(regex, "*".repeat(Math.max(0, obfuscateAmount)));
    }

  	public static String obfuscateLastThree(String s) {
  		return obfuscate(s, true, 3);
  	}
}