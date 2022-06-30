package iris.client_bff.core.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import iris.client_bff.core.validation.AttackDetector.MessageDataPayload;
import iris.client_bff.core.validation.AttackDetector.Password;
import iris.client_bff.core.validation.AttackDetector.Phone;
import iris.client_bff.core.validation.NoSignOfAttack.NoSignOfAttackValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = NoSignOfAttackValidator.class)
@Documented
public @interface NoSignOfAttack {

	String message()

	default "{iris.validation.constraints.NoSignOfAttack.message}";

	Class<?>[] groups() default {};

	boolean obfuscateLogging() default true;

	Class<? extends Payload>[] payload() default {};

	static class NoSignOfAttackValidator implements ConstraintValidator<NoSignOfAttack, String> {

		@Autowired
		private AttackDetector attackDetector;

		private Class<? extends Payload> type;
		private boolean obfuscateLogging;

		@Override
		public void initialize(NoSignOfAttack constraintAnnotation) {

			obfuscateLogging = constraintAnnotation.obfuscateLogging();
			var payloads = constraintAnnotation.payload();

			Assert.isTrue(payloads.length <= 1, "Only one type can be defined!");

			if (payloads.length == 1) {
				this.type = payloads[0];
			}
		}

		@Override
		public boolean isValid(final String text, final ConstraintValidatorContext context) {

			var path = "";
			if (context instanceof ConstraintValidatorContextImpl impl) {
				path = impl.getConstraintViolationCreationContexts().get(0).getPath().asString();
			}

			if (type == Phone.class) {
				return !attackDetector.isPossibleAttackForPhone(text, path, obfuscateLogging);
			}
			if (type == Password.class) {
				return !attackDetector.isPossibleAttackForPassword(text, path);
			}
			if (type == MessageDataPayload.class) {
				return !attackDetector.isPossibleAttackForMessageDataPayload(text, path, obfuscateLogging);
			}

			return !attackDetector.isPossibleAttack(text, path, obfuscateLogging);
		}
	}
}
