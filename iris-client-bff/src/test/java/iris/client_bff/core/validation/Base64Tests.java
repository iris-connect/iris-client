package iris.client_bff.core.validation;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.core.extension.ValidatorExtension;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Jens Kutzsche
 */
@ExtendWith(ValidatorExtension.class)
@Tag("constraints")
@DisplayName("Base64 validation")
@RequiredArgsConstructor
class Base64Tests {

	static Validator validator;

	@ParameterizedTest(name = "\"{0}\"")
	@NullSource
	@ValueSource(strings = { "", "YWJjZGVmZ2hpams=", "MDEyMyA0NTY3IDg5", "TWF4LU11c3Rlcm1hbm4tU3RyYcOfZQ==",
			"TWF4LU11c3Rlcm1hbm4tU3RyYcOfZQ==" })
	@DisplayName("with valide value")
	void validate_withValidValue_getNoViolations(String value) {

		var violations = validator.validate(new TestObject(value));

		assertThat(violations).isEmpty();
	}

	@ParameterizedTest(name = "\"{0}\"")
	@ValueSource(strings = { "Hello World", "XXX%", "123.456", " 	 " })
	@DisplayName("with invalide value")
	void validate_withInvalidValue_getViolations(String value) {

		var violations = validator.validate(new TestObject(value));

		assertThat(violations).singleElement().extracting(ConstraintViolation<TestObject>::getMessage).asString()
				.startsWith("No valid Base64 encoding");
	}

	record TestObject(@Base64 String data) {}
}
