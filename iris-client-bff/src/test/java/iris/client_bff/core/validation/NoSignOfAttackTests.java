package iris.client_bff.core.validation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.MemoryAppender;
import iris.client_bff.core.alert.AlertService;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@Tag("constraints")
@DisplayName("No sign of attack validation")
@RequiredArgsConstructor
class NoSignOfAttackTests {

	final Validator validator;

	@MockBean
	final AlertService alerts;

	private MemoryAppender logAppender;

	@BeforeAll
	static void init() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@BeforeEach
	public void setup() {
		Logger logger = (Logger) LoggerFactory.getLogger(AttackDetector.class);
		logAppender = new MemoryAppender();
		logAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.setLevel(Level.DEBUG);
		logger.addAppender(logAppender);
		logAppender.start();
	}

	@ParameterizedTest(name = "\"{0}\"")
	@ValueSource(strings = { "Test", "Jens Kutzsche", "01662 Meißen", "0123 4567 89", "Max-Mustermann-Straße",
			"Nummer #56", "test@test.de", "0+8+15", "", " 	 " })
	@DisplayName("with valide value")
	void validate_withValidValue_getNoViolations(String value) {

		var violations = validator.validate(new TestObject(value));

		assertThat(violations).isEmpty();
	}

	@ParameterizedTest(name = "{0} ({1})")
	@CsvSource({ "@test.de, Forbidden symbol at first position",
			"+49 323 2323, Forbidden symbol at first position", "#2332332, Forbidden symbol at first position",
			"_test, Forbidden symbol at first position",
			"-test, Forbidden symbol at first position",
			"=test, Forbidden symbol at first position", "(test, Forbidden symbol at first position",
			"<test, Forbidden symbol at first position", "{test, Forbidden symbol at first position",
			"[test, Forbidden symbol at first position", "Test<SCRIPT iuaeuia, Forbidden keyword in text",
			"Test JAVASCRIPT: iuaeuia, Forbidden keyword in text",
			"Test select xyz from iuaeuia, Forbidden keyword in text" })
	@DisplayName("with invalide value")
	void validate_withInvalidValue_getViolations(String value, String hint) {

		var violations = validator.validate(new TestObject(value));

		assertThat(violations).singleElement().extracting(ConstraintViolation<TestObject>::getMessage).asString()
				.startsWith("Contains illegal characters");

		assertThat(logAppender.contains("Eingabedaten sind ungültig - data:", Level.WARN)).isTrue();

		verify(alerts).createAlertMessage(anyString(), anyString());
		verifyNoMoreInteractions(alerts);
	}

	record TestObject(@NoSignOfAttack String data) {}
}
