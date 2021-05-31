package iris.client_bff.core.mail;

import java.util.Locale;
import java.util.Map;

import org.springframework.lang.Nullable;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
public interface EmailTemplates {

	/**
	 * Expands the template with the given {@link Key} and placeholders in the language of the given locale.
	 *
	 * @param key must not be {@literal null}.
	 * @param placeholders must not be {@literal null}.
	 * @param locale can be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	String expandTemplate(Key key, Map<String, Object> placeholders, @Nullable Locale locale);

	/**
	 * A template key.
	 *
	 * @author Oliver Drotbohm
	 * @author Jens Kutzsche
	 */
	interface Key {

		/**
		 * @return The current {@link Key} as String.
		 */
		String getKey();
	}

	/**
	 * Predefined keys to be usable with the application.
	 *
	 * @author Oliver Drotbohm
	 * @author Jens Kutzsche
	 */
	enum Keys implements Key {

		;

		@Override
		public String getKey() {
			return name().toLowerCase(Locale.US).replace('_', '-').concat(".ftl");
		}
	}
}
