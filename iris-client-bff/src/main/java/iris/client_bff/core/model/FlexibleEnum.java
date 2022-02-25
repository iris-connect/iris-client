package iris.client_bff.core.model;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @author Jens Kutzsche
 */
public interface FlexibleEnum {

	static String cleanStringForEnum(String string) {

		var cleanString = replaceChars(string, '-', '_');
		cleanString = camelToSnake(cleanString);

		return cleanString.toUpperCase();
	}

	static String camelToSnake(String str) {
		return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
	}
}
