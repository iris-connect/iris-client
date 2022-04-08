package iris.client_bff.core.extensions;

import static org.junit.platform.commons.util.ReflectionUtils.*;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode;

/**
 * {@code ValidatorExtension} is a JUnit Jupiter extension that creates a {@link Validator} for every
 * <strong>static</strong> field of this type in the test class.
 * <p>
 * The {@link MessageInterpolator} is set and use the {@code messages_en.properties}. English is set as locale for the
 * interpolation in the tests.
 * </p>
 *
 * @author Jens Kutzsche
 */
public class ValidatorExtension implements BeforeAllCallback {

	/**
	 * Perform field injection for {@code static} fields (i.e., class fields) of type {@link Validator}.
	 */
	@Override
	public void beforeAll(ExtensionContext context) {
		injectStaticFields(context.getRequiredTestClass());
	}

	private void injectStaticFields(Class<?> testClass) {
		injectFields(null, testClass, ReflectionUtils::isStatic);
	}

	private void injectFields(Object testInstance, Class<?> testClass,
			Predicate<Field> predicate) {

		findFields(testClass, predicate.and(this::isValidatorField), HierarchyTraversalMode.TOP_DOWN)
				.forEach(field -> {

					try {
						makeAccessible(field).set(testInstance, createValidator());
					} catch (Throwable t) {
						ExceptionUtils.throwAsUncheckedException(t);
					}
				});
	}

	private boolean isValidatorField(Field field) {
		return field.getType() == Validator.class;
	}

	private Validator createValidator() {

		var configure = Validation.byDefaultProvider().configure();
		configure.messageInterpolator(
				new ResourceBundleMessageInterpolator(locale -> ResourceBundle.getBundle("messages", Locale.ENGLISH)));

		return configure.buildValidatorFactory().getValidator();
	}
}
