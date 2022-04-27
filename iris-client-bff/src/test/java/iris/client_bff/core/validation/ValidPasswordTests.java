package iris.client_bff.core.validation;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.core.extensions.ValidatorExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Jens Kutzsche
 */
@ExtendWith(ValidatorExtension.class)
@Tag("constraints")
@DisplayName("Password validation")
class ValidPasswordTests {

	static Validator validator;

	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "Password12A_", "X1ab__ae", "X1aba2ae", "X1ab$aae", "x-ab$aae", "X-AB$AAE" })
	@DisplayName("with valide password")
	void validate_withValidPw_getNoViolations(String pw) {

		var violations = validator.validate(new TestObject(pw));

		assertThat(violations).isEmpty();
	}

	@ParameterizedTest(name = "{0} ({1})")
	@CsvSource({ "Password, Numbers or special characters are missing",
			"X1aba2a, to short", "X1aba2ae+, + not allowed",
			"X1ab€2 ae, Spaces not allowed",
			"X1ab€2\u00A0ae, No-break space not allowed",
			"X1ab€2\u202fae, Narrow no-break space not allowed" })
	@DisplayName("with invalide password")
	void validate_withInvalidPw_getViolations(String pw, String hint) {

		var violations = validator.validate(new TestObject(pw));

		assertThat(violations).singleElement().extracting(ConstraintViolation<TestObject>::getMessage).asString()
				.startsWith("The specified password does not follow the password policy");
	}

	record TestObject(@ValidPassword String pw) {}
}
