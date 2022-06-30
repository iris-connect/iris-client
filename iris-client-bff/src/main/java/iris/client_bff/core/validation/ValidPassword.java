package iris.client_bff.core.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Pattern(
		regexp = "^(?=.*[0123456789_\\-#()@§!$].*[0123456789_\\-#()@§!$])(?=.*[ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜẞabcdefghijklmnopqrstuvwxyzäöüß])([ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜẞ]|[abcdefghijklmnopqrstuvwxyzäöüß]|[0123456789]|[_\\-#()@§!$]){8,}$",
		message = "{iris.validation.constraints.ValidPassword.message}")
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
public @interface ValidPassword {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
