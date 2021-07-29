package iris.client_bff.core.utils;

import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationHelper {

	public static boolean isUUIDInputValid(String id, String message) {
		String uuidRegex = "([0123456789abcdef]{8})-([0123456789abcdef]{4})-([0123456789abcdef]{4})-([0123456789abcdef]{4})-([0123456789abcdef]{12})";

		if (id.matches(uuidRegex)) {
			return true;
		}

		log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + message);
		return false;
	}

	public static boolean isPossibleAttackForRequiredValue(String input, String message) {
		if (input == null || input.length() <= 0) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + message);
			return true;
		}

		return isPossibleAttack(input, message);
	}

	public static boolean isPossibleAttack(String input, String message) {
		String[] forbiddenSymbolsArray = {
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

		if (input == null) {
			return false;
		}

		String inputUpper = input.toUpperCase();
		if (inputUpper.contains("<SCRIPT")
			|| inputUpper.contains("SELECT") && inputUpper.contains("FROM")
			|| StringUtils.startsWithAny(input, forbiddenSymbolsArray)) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + message);
			return true;
		}

		return false;
	}

	public static boolean checkInputNameConventions(String input) {
		String[] forbiddenSymbolsArray = {
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
			"_",
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
		Stream<String> forbiddenSymbolsStream = Arrays.stream(forbiddenSymbolsArray);
		int forbiddenSymbolCounter = (int) forbiddenSymbolsStream.filter(symbol -> input.contains(symbol) == true).count();

		if (forbiddenSymbolCounter > 0)
			return false;

		return true;
	}
}
