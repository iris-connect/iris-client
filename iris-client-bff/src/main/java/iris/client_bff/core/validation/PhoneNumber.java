package iris.client_bff.core.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import iris.client_bff.core.validation.PhoneNumber.PhoneNumberValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface PhoneNumber {

	String message() default "{iris.validation.constraints.PhoneNumber.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public static class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

		private PhoneNumberUtil util = PhoneNumberUtil.getInstance();

		@Override
		public boolean isValid(final String text, final ConstraintValidatorContext context) {

			if (text == null) {
				return true;
			}

			try {

				var number = util.parse(text, "ZZ");
				return util.isValidNumber(number);

			} catch (NumberParseException e) {
				return false;
			}
		}
	}
}
