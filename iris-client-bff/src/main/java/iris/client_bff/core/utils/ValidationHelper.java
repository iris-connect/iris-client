package iris.client_bff.core.utils;

import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationHelper {

	private static final String UUID_REGEX = "([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})";
	private static final String[] FORBIDDEN_SYMBOLS = {
			"=",
			"<",
			">",
			"!",
			"\"",
			"§",
			"$",
			"%",
			"&",
			"/",
			"(",
			")",
			"?",
			"´",
			"`",
			"¿",
			"≠",
			"¯",
			"}",
			"·",
			"{",
			"˜",
			"\\",
			"]",
			"^",
			"ﬁ",
			"[",
			"¢",
			"¶",
			"“",
			"¡",
			"¬",
			"”",
			"#",
			"£",
			"+",
			"*",
			"±",
			"",
			"‘",
			"’",
			"'",
			"-",
			"_",
			".",
			":",
			"…",
			"÷",
			"∞",
			";",
			"˛",
			"æ",
			"Æ",
			"œ",
			"Œ",
			"@",
			"•",
			"°",
			"„" };

	public static boolean isUUIDInputValid(String id, String idName) {

		if (id.matches(UUID_REGEX)) {
			return true;
		}

		log.warn(ErrorMessages.INVALID_INPUT + idName + id);
		return false;
	}

	public static boolean isPossibleAttackForRequiredValue(String input, String message) {
		if (input == null || input.length() <= 0) {
			log.warn(ErrorMessages.INVALID_INPUT + message);
			return true;
		}

		return isPossibleAttack(input, message);
	}

	public static boolean isPossibleAttack(String input, String message) {
		if (input == null) {
			return false;
		}

		String inputUpper = input.toUpperCase();
		if (inputUpper.contains("<SCRIPT")
			|| inputUpper.contains("SELECT") && inputUpper.contains("FROM")
			|| StringUtils.startsWithAny(input, FORBIDDEN_SYMBOLS)) {
			log.warn(ErrorMessages.INVALID_INPUT + message);
			return true;
		}

		return false;
	}
}
