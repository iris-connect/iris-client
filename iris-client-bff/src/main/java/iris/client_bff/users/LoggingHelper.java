package iris.client_bff.users;

public class LoggingHelper {

	private LoggingHelper() {}

	public static String obfuscate(String s) {
		return (s == null) ? null : s.replaceFirst(".{0,3}$", "***");
	}
}
