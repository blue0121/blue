package blue.base.internal.core.path.route;

import blue.base.core.path.MatchResult;
import blue.base.core.path.RouteMatcher;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class HttpRouteMatcher implements RouteMatcher {
	private static Logger logger = LoggerFactory.getLogger(HttpRouteMatcher.class);

	public static final String BRACE_LEFT = "{";
	public static final char CHAR_BRACE_LEFT = BRACE_LEFT.charAt(0);
	public static final String BRACE_RIGHT = "}";
	public static final char CHAR_BRACE_RIGHT = BRACE_RIGHT.charAt(0);
	public static final String WILDCARD = "*";
	public static final String MULTI_WILDCARD = "**";
	public static final char CHAR_WILDCARD = WILDCARD.charAt(0);


	public HttpRouteMatcher() {
	}

	@Override
	public boolean validate(String path) {
		HttpValidator validator = new HttpValidator(path);
		return validator.validate();
	}

	@Override
	public boolean add(String pattern, Object param) {
		HttpValidator validator = new HttpValidator(pattern);
		if (!validator.validate()) {
			return false;
		}
		int index = -1;
		int start = 1;
		List<String> list = new ArrayList<>();
		while ((index = pattern.indexOf(RouteMatcher.CHAR_SLASH, start)) != -1) {
			String word = pattern.substring(start, index);
			System.out.println(word);
			list.add(word);
			start = index + 1;
		}
		String word = pattern.substring(start);
		list.add(word);

		return true;
	}

	@Override
	public MatchResult match(String route) {
		AssertUtil.notEmpty(route, "Route");

		return null;
	}

}
