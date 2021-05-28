package blue.base.internal.core.path.route;

import blue.base.core.path.RouteMatcher;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class HttpValidator {
	private static Logger logger = LoggerFactory.getLogger(HttpValidator.class);

	public enum HttpState {
		START,
		SLASH,
		BRACE_START,
		BRACE_END,
		WILDCARD,
		MULTI_WILDCARD,
	}

	private final String path;
	private char current;
	private HttpState state = HttpState.START;

	public HttpValidator(String path) {
		AssertUtil.notEmpty(path, "Path");
		this.path = this.trim(path);
	}

	public boolean validate() {
		logger.debug("========== Start: {} ==========", path);
		int len = path.length();
		for (int i = 0; i < len; i++) {
			current = path.charAt(i);
			logger.debug("State: {}, char: {}", state, current);
			if (state == HttpState.BRACE_START) {
				if (current == RouteMatcher.CHAR_SLASH || current == HttpRouteMatcher.CHAR_BRACE_LEFT
						|| current == HttpRouteMatcher.CHAR_WILDCARD) {
					return false;
				}
			} else if (state == HttpState.WILDCARD) {
				if (current == HttpRouteMatcher.CHAR_BRACE_RIGHT) {
					return false;
				}
				if (current == HttpRouteMatcher.CHAR_WILDCARD) {
					state = HttpState.MULTI_WILDCARD;
					continue;
				}

			} else if (state == HttpState.MULTI_WILDCARD) {
				if (current == HttpRouteMatcher.CHAR_WILDCARD || current == HttpRouteMatcher.CHAR_BRACE_RIGHT) {
					return false;
				}
			} else if (state == HttpState.BRACE_END) {
				if (current == HttpRouteMatcher.CHAR_WILDCARD) {
					return false;
				}
			} else {
				if (current == HttpRouteMatcher.CHAR_BRACE_RIGHT) {
					return false;
				}
			}
			if (i == len - 1) {
				if (current == HttpRouteMatcher.CHAR_BRACE_LEFT) {
					return false;
				}
				if (state == HttpState.BRACE_START && current != HttpRouteMatcher.CHAR_BRACE_RIGHT) {
					return false;
				}
			}
			this.setCurrentState();
		}
		return true;
	}

	private void setCurrentState() {
		if (current == RouteMatcher.CHAR_SLASH) {
			state = HttpState.SLASH;
		} else if (current == HttpRouteMatcher.CHAR_BRACE_LEFT) {
			state = HttpState.BRACE_START;
		} else if (current == HttpRouteMatcher.CHAR_BRACE_RIGHT) {
			state = HttpState.BRACE_END;
		} else if (current == HttpRouteMatcher.CHAR_WILDCARD) {
			state = HttpState.WILDCARD;
		}

	}

	private String trim(String path) {
		StringBuilder trimPath = new StringBuilder(path.length());
		if (path.charAt(0) != RouteMatcher.CHAR_SLASH) {
			trimPath.append(RouteMatcher.CHAR_SLASH);
		}
		char last = 0;
		int len = path.length();
		for (int i = 0; i < len; i++) {
			char current = path.charAt(i);
			if (current == RouteMatcher.CHAR_SLASH) {
				if (last == RouteMatcher.CHAR_SLASH || i == len - 1) {
					continue;
				}
			}
			trimPath.append(current);
			last = current;
		}
		return trimPath.toString();
	}

	public String getTrimPath() {
		return path;
	}
}
