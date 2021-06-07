package blue.base.internal.core.path.route;

import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2021-06-05
 */
public class HttpPathParser {
	private static Logger logger = LoggerFactory.getLogger(HttpPathParser.class);

	public static final String SLASH = "/";
	public static final char CHAR_SLASH = SLASH.charAt(0);
	public static final String BRACE_LEFT = "{";
	public static final char CHAR_BRACE_LEFT = BRACE_LEFT.charAt(0);
	public static final String BRACE_RIGHT = "}";
	public static final char CHAR_BRACE_RIGHT = BRACE_RIGHT.charAt(0);
	public static final String REGEX_SEPARATE = ":";
	public static final char CHAR_REGEX_SEPARATE = REGEX_SEPARATE.charAt(0);

	private final String path;
	private char current;
	private StringBuilder word;
	private HttpPathState state = HttpPathState.START;

	public HttpPathParser(String path) {
		AssertUtil.notNull(path, "Path");
		path = path.trim();
		AssertUtil.notEmpty(path, "Path");
		this.path = this.trim(path);
	}

	public boolean parse() {
		logger.debug("========== Start: {} ==========", path);
		int len = path.length();
		for (int i = 0; i < len; i++) {
			current = path.charAt(i);
			logger.debug("-- State: {}, char: {}, word: {}", state, current, word);
			switch (state) {
				case SLASH:
					word = new StringBuilder();
					word.append(current);
					state = HttpPathState.LETTER;
					break;
				case BRACE_START:
					if (current == CHAR_BRACE_LEFT || current == CHAR_BRACE_RIGHT || current == CHAR_SLASH || current == CHAR_REGEX_SEPARATE) {
						return false;
					}
					word = new StringBuilder();
					word.append(current);
					state = HttpPathState.BRACE_VARIABLE;
					break;
				case BRACE_VARIABLE:
					if (current == CHAR_REGEX_SEPARATE) {
						state = HttpPathState.REGEX_LETTER;
						// TODO: variable
						word = new StringBuilder();
					} else {
						word.append(current);
						System.out.println(">>>>>>>> " + word);
					}
					break;
				case LETTER:
				case REGEX_LETTER:
					word.append(current);
					break;
				case BRACE_END:
					if (current == CHAR_BRACE_LEFT) {
						return false;
					}
					state = HttpPathState.LETTER;
					break;
				default:
					if (current == CHAR_BRACE_RIGHT) {
						return false;
					}
					break;
			}
			if (i == len - 1) {
				if (current == CHAR_BRACE_LEFT) {
					return false;
				}
				if (current == CHAR_BRACE_RIGHT) {
					if (state != HttpPathState.BRACE_VARIABLE && state != HttpPathState.REGEX_LETTER) {
						return false;
					}
				} else {
					if (state == HttpPathState.BRACE_START || state == HttpPathState.BRACE_VARIABLE) {
						return false;
					}
				}
			}
			this.setCurrentState();
			logger.debug("State: {}, char: {}, word: {}", state, current, word);
		}
		return true;
	}

	private void setCurrentState() {
		if (current == CHAR_SLASH) {
			state = HttpPathState.SLASH;
		} else if (current == CHAR_BRACE_LEFT) {
			state = HttpPathState.BRACE_START;
		} else if (current == CHAR_BRACE_RIGHT) {
			state = HttpPathState.BRACE_END;
		}
	}

	private String trim(String path) {
		StringBuilder trimPath = new StringBuilder(path.length());
		if (path.charAt(0) != CHAR_SLASH) {
			trimPath.append(CHAR_SLASH);
		}
		char last = 0;
		int len = path.length();
		for (int i = 0; i < len; i++) {
			char current = path.charAt(i);
			if (current == CHAR_SLASH) {
				if (last == CHAR_SLASH || i == len - 1) {
					continue;
				}
			}
			trimPath.append(current);
			last = current;
		}
		return trimPath.toString();
	}

	public MatchType getMatchType() {
		if (path.contains(HttpRouteMatcher.BRACE_LEFT)) {
			return MatchType.VARIABLE;
		}

		return MatchType.EXACTLY;
	}

	public String getTrimPath() {
		return path;
	}
}
