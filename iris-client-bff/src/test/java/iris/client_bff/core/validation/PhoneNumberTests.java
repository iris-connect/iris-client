package iris.client_bff.core.validation;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.core.extensions.ValidatorExtension;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Jens Kutzsche
 */
@ExtendWith(ValidatorExtension.class)
@Tag("constraints")
@DisplayName("Phone number validation")
@RequiredArgsConstructor
class PhoneNumberTests {

	static Validator validator;

	@ParameterizedTest(name = "\"{0}\"")
	@ValueSource(
			strings = { "+49 351 67833", "+4935112345678", "+49 351/12345678", "+49 (351) 12345678", "+49172 11156789",
					"+49/172/11156789", "+61403123456", "+43 316 787 400" })
	@DisplayName("with valide value")
	void validate_withValidValue_getNoViolations(String value) {

		var violations = validator.validate(new TestObject(value));

		assertThat(violations).isEmpty();
	}

	@ParameterizedTest(name = "\"{0}\"")
	@ValueSource(strings = { "", "+49351%56789", "+49 351 473+11", "+49123456*89", "0", "1234567",
			"+49172 111567"/*to short*/, "+49 12345 67833" /*area code invalid*/, "035156789" /*missing country code*/,
			"(0351) 56789", "0172 11156789", "0172/11156789", "0222 6578-22", "0351A56789" })
	@DisplayName("with invalide value")
	void validate_withInvalidValue_getViolations(String value) {

		var violations = validator.validate(new TestObject(value));

		assertThat(violations).singleElement()
				.extracting(ConstraintViolation<TestObject>::getMessage)
				.asString()
				.startsWith("Invalid phone number");
	}

	record TestObject(@PhoneNumber String data) {}
}
