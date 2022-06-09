package iris.client_bff.core.validation;

import lombok.NonNull;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

/**
 * This class supports the validation of property classes annotated with `@ConfigurationProperties`. Keys used in the
 * message properties of the validation annotations `{...}` are resolved and translated. This is different when using
 * `@Validated` and is not done there. So if a custom translated message is needed, it should be derived from this class
 * and not use `@Validated`.
 *
 * @author Jens Kutzsche
 */
public class ValidatedProperties implements Validator {

	private final SpringValidatorAdapter validatorAdapter;
	private final Class<?> type;

	public ValidatedProperties(@NonNull Class<?> type, @NonNull javax.validation.Validator validator) {
		this.type = type;
		validatorAdapter = new SpringValidatorAdapter(validator);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return type.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		validatorAdapter.validate(target, errors);
	}
}
