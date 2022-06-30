package iris.client_bff.core.validation;

import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.config.RPCClientProperties;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.core.log.LogHelper;
import iris.client_bff.core.messages.ErrorMessages;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Payload;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AttackDetector {

	public interface Phone extends Payload {}

	public interface Password extends Payload {}

	public interface MessageDataPayload extends Payload {}

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
			{ "DATA:" },
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

	private final AlertService alertService;
	private final RPCClientProperties clientProperties;

	AttackDetector(@Lazy AlertService alertService, RPCClientProperties clientProperties) {
		this.alertService = alertService;
		this.clientProperties = clientProperties;
	}

	public boolean isPostOutOfLimit(List<?> list, String client, long postLimit, long warnLimit, String type) {

		if (list == null) {
			return false;
		}

		if (list.size() > postLimit) {

			var msg = String.format(
					"Input from client `%s` contains %d %s at once. We prevent this as a possible attack! {threshold = %d}",
					client, list.size(), type, postLimit);
			alertService.createAlertTicketAndMessage(String.format("Input validation - to many %s posted", type), msg);

			return true;
		}

		if (list.size() > warnLimit) {

			var msg = String.format(
					"Input from client `%s` contains %d %s at once. This could be an indication of an attack! {threshold = %d}",
					client, list.size(), type, postLimit);
			alertService.createAlertMessage(String.format("Input validation - warning about many %s posts", type), msg);
		}

		return false;
	}

	public boolean isPossibleAttackForMessageDataPayload(String input, String field, boolean obfuscateLogging) {

		if (input == null) {
			return false;
		}

		// strip json structure and extract keys & values
		String[] inputValues = input.replaceAll("[\":,{}\\[\\]]+", ",").split(",");

		// remove "+" from forbiddenSymbols as it is allowed for phone numbers
		String[] forbiddenSymbols = ArrayUtils.removeElement(FORBIDDEN_SYMBOLS, "+");

		// check if any json key or value is a possible attack
		return Arrays.stream(inputValues)
				.anyMatch(v -> isPossibleAttack(v, field, obfuscateLogging, FORBIDDEN_KEYWORD_TUPLES, forbiddenSymbols));
	}

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
							field, clientProperties.getOwnEndpoint(), logString));

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
			if (indexOfIgnoreCase(input, keyword) < 0) {
				return Optional.empty();
			}
		}

		var lastKeyword = keywordTuple[keywordTuple.length - 1];

		return Optional
				.of(Range.between(indexOfIgnoreCase(input, keywordTuple[0]),
						indexOfIgnoreCase(input, lastKeyword) + lastKeyword.length() - 1));
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
