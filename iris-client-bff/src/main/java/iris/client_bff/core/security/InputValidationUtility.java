package iris.client_bff.core.security;

import java.util.Arrays;
import java.util.stream.Stream;

public class InputValidationUtility {

	public boolean isUUIDInputValid(String id) {
		String uuidRegex = "([0123456789abcdef]{8})-([0123456789abcdef]{4})-([0123456789abcdef]{4})-([0123456789abcdef]{4})-([0123456789abcdef]{12})";
		return id.matches(uuidRegex);
	}

	public boolean checkInputForAttacks(String input) {
		if (input == null)
			return false;

		if (input.length() <= 0)
			return false;

		if (input.contains("<script"))
			return false;

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
		Stream<String> forbiddenSymbolsStream = Arrays.stream(forbiddenSymbolsArray);
		int forbiddenSymbolCounter = (int) forbiddenSymbolsStream.filter(symbol -> input.startsWith(symbol) == true).count();

		if (forbiddenSymbolCounter > 0)
			return false;

		return true;
	}

	public boolean checkInputNameConventions(String input) {
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
