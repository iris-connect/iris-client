package iris.client_bff.matchers;

import static org.hamcrest.core.AnyOf.*;
import static org.hamcrest.core.IsNull.*;

import java.util.UUID;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Jens Kutzsche
 */
public final class IsUuid extends TypeSafeMatcher<String> {

	private static final IsUuid UUID_INSTANCE = new IsUuid();
	private static final Matcher<String> NULL_OR_UUID_INSTANCE = anyOf(nullValue(), UUID_INSTANCE);

	@Override
	public void describeTo(Description description) {
		description.appendText("a valid UUID");
	}

	@Override
	protected boolean matchesSafely(String item) {

		try {
			UUID.fromString(item);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Creates a matcher of {@link String} that matches when the examined string is a valid {@link UUID}.
	 */
	public static Matcher<String> uuidString() {
		return UUID_INSTANCE;
	}

	/**
	 * Creates a matcher of {@link String} that matches when the examined string is <code>null</code>, or a valid
	 * {@link UUID}.
	 */
	public static Matcher<String> uuidOrNullString() {
		return NULL_OR_UUID_INSTANCE;
	}
}
