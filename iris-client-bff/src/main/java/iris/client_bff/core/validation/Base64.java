package iris.client_bff.core.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import iris.client_bff.core.validation.Base64.Base64Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.util.Base64Utils;

/**
 * Valid if all characters in the String are valid characters in the Base64 alphabet or if the String is Null or empty.
 *
 * @author Jens Kutzsche
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = Base64Validator.class)
@Documented
public @interface Base64 {

	String message() default "{iris.validation.constraints.Base64.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public static class Base64Validator implements ConstraintValidator<Base64, String> {

		@Override
		public boolean isValid(final String text, final ConstraintValidatorContext context) {

			if (text == null) {
				return true;
			}

			try {

				Base64Utils.decodeFromString(text);
				return true;

			} catch (IllegalArgumentException e) {
				return false;
			}
		}
	}
}
