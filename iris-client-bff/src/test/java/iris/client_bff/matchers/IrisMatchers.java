package iris.client_bff.matchers;

import static org.hamcrest.Matchers.*;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class IrisMatchers {

	public static org.hamcrest.Matcher<String> isUuid() {
		return IsUuid.uuidString();
	}

	public static org.hamcrest.Matcher<String> isCatWith(String proxyUrl) {
		return allOf(
				endsWith(proxyUrl),
				new HasToken(isUuid()));
	}

	static class HasToken extends FeatureMatcher<String, String> {
		public HasToken(Matcher<? super String> tokenMatcher) {
			super(tokenMatcher, "with token part", "token");
		}

		@Override
		protected String featureValueOf(String actual) {
			return StringUtils.substringBefore(actual, ".");
		}
	}
}
