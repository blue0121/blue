package blue.base.internal.core.path.route;

import blue.base.core.path.MatchResult;
import blue.base.core.path.RouteMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

	private final Map<String, Object> pathMap = new HashMap<>();
	private final TrieRouter trieRouter = new TrieRouter();

	public HttpRouteMatcher() {
	}

	@Override
	public boolean validate(String path) {
		HttpPathParser parser = new HttpPathParser(path);
		return parser.parse();
	}

	@Override
	public boolean add(String pattern, Object param) {
		HttpPathParser parser = new HttpPathParser(pattern);
		if (!parser.parse()) {
			return false;
		}
		if (parser.getMatchType() == MatchType.EXACTLY) {
			pathMap.put(parser.getTrimPath(), param);
		} else {
			trieRouter.add(pattern, param);
		}

		return true;
	}

	@Override
	public MatchResult match(String path) {
		HttpPathParser validator = new HttpPathParser(path);
		String trimPath = validator.getTrimPath();
		if (pathMap.containsKey(trimPath)) {
			Object param = pathMap.get(trimPath);
			return new DefaultMatchResult(path, trimPath, param, null);
		}


		return null;
	}

}
