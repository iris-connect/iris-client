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
	 * @param key
	 *            must not be {@literal null}.
	 * @param locale
	 *            can be {@literal null}.
	 * @param placeholders
	 *            must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	String expandTemplate(Key key, @Nullable Locale locale, Map<String, ? extends Object> placeholders);

	/**
	 * Returns the type of the template with the given {@link Key} in the language of the given locale.
	 *
	 * @param key
	 *            must not be {@literal null}.
	 * @param locale
	 *            can be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	EmailType getTemplateType(Key key, @Nullable Locale locale);

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
	 * Predefined keys to be usable with the application. The key name is converted to the template name. Thereby "_FTL"
	 * becomes the file extension ".ftl" and other "_" become "-". Always use one of Freemarker's two default endings
	 * "ftlh" for HTML templates - in this case HTML mails - and "ftl" for text mails.
	 *
	 * @author Oliver Drotbohm
	 * @author Jens Kutzsche
	 */
	enum Keys
		implements
		Key {

		CASE_DATA_RECEIVED_MAIL_FTLH,
		EVENT_DATA_RECEIVED_MAIL_FTLH;

		@Override
		public String getKey() {
			return name().toLowerCase(Locale.US).replace("_ftl", ".ftl").replace('_', '-');
		}
	}

	enum EmailType {
		HTML,
		TEXT;
	}
}
