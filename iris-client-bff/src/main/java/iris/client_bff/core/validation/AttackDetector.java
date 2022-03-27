package iris.client_bff.core.validation;

import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.config.CentralConfigurationService;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.core.log.LogHelper;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor()
class AttackDetector {

	private final AlertService alertService;
	private final CentralConfigurationService configService;

	static final String[] PW_SYMBOLS = "_-#()@§!".split("(?!^)");
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

	private static final String[][] FORBIDDEN_KEYWORD_TUPLES = {
			{ "<SCRIPT" },
			{ "JAVASCRIPT:" },
			{ "SELECT", "FROM" },
			{ "INSERT", "INTO" },
			{ "UPDATE", "SET" },
			{ "DELETE", "FROM" },
			{ "CREATE", "TABLE" },
			{ "DROP", "TABLE" },
			{ "ALTER", "TABLE" },
			{ "CREATE", "INDEX" },
			{ "DROP", "INDEX" },
			{ "CREATE", "VIEW" },
			{ "DROP", "VIEW" }
	};

	public boolean isPossibleAttack(String input, String field, boolean obfuscateLogging) {
		return isPossibleAttack(input, field, obfuscateLogging, FORBIDDEN_KEYWORD_TUPLES, FORBIDDEN_SYMBOLS);
	}

	public boolean isPossibleAttackForPassword(String input, String field) {

		return isPossibleAttack(input, field, true, FORBIDDEN_KEYWORD_TUPLES,
				ArrayUtils.removeElements(FORBIDDEN_SYMBOLS, PW_SYMBOLS));
	}

	public boolean isPossibleAttackForPhone(String input, String field, boolean obfuscateLogging) {

		return isPossibleAttack(input, field, obfuscateLogging, FORBIDDEN_KEYWORD_TUPLES,
				ArrayUtils.removeElement(FORBIDDEN_SYMBOLS, "+"));
	}

	boolean isPossibleAttack(String input, String field, boolean obfuscateLogging,
			String[][] forbiddenKeywordTuples, String[] forbiddenSymbols) {

		if (input == null) {
			return false;
		}

		// Test for attacks
		String inputUpper = input.toUpperCase();
		Optional<Range<Integer>> range = findAnyOfKeywordTuples(inputUpper, forbiddenKeywordTuples);
		if (range.isEmpty()) {
			range = findSymbolsAtStart(input, forbiddenSymbols);
		}

		if (range.isPresent()) {
			String logString = calculateLogableValue(input, obfuscateLogging, range.get());
			log.warn(ErrorMessages.INVALID_INPUT + " - {}: {}", field, logString);

			alertService.createAlertMessage("Input validation - possible attack",
					String.format(
							"Input `%s` in health department with abbreviation `%s` contains the character or keyword `%s` that is a potential attack!",
							field, configService.getAbbreviation(), logString));

			return true;
		}

		return false;
	}

	/**
	 * Searches for any occurrence of given keyword tuples and stops at first match.
	 * 
	 * @param str String to be tested
	 * @param keywordTuples Array of tuples with keywords to find
	 * @return If found: range of first matching tuple in tested string starting at beginning of first keyword, ending at
	 *         end of last keyword.
	 */
	private static Optional<Range<Integer>> findAnyOfKeywordTuples(String str, String[][] keywordTuples) {

		return Arrays.stream(keywordTuples)
				.map(it -> findKeywordTuple(str, it))
				.flatMap(Optional<Range<Integer>>::stream)
				.findFirst();
	}

	/**
	 * Searches for the keyword tuple in the input string.
	 * 
	 * @param input String to be tested
	 * @param keywordTuple tuple with keywords to find
	 * @return If found: range in tested string starting at beginning of first keyword, ending at end of last keyword.
	 */
	private static Optional<Range<Integer>> findKeywordTuple(String input, String[] keywordTuple) {

		for (var keyword : keywordTuple) {
			if (indexOf(input, keyword) < 0) {
				return Optional.empty();
			}
		}

		var lastKeyword = keywordTuple[keywordTuple.length - 1];

		return Optional
				.of(Range.between(indexOf(input, keywordTuple[0]), indexOf(input, lastKeyword) + lastKeyword.length() - 1));
	}

	private static Optional<Range<Integer>> findSymbolsAtStart(String input, String[] forbiddenSymbolsArray) {

		if (StringUtils.startsWithAny(input, forbiddenSymbolsArray)) {
			return Optional.of(Range.is(0));
		}

		return Optional.empty();
	}

	private static String calculateLogableValue(String input, boolean obfuscateLogging, Range<Integer> range) {

		return obfuscateLogging
				? LogHelper.obfuscateWithExcludedRange(input, range)
				: input;
	}
}
