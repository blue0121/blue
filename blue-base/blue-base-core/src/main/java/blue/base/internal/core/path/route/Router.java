package blue.base.internal.core.path.route;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class Router {

	private final int level;
	private final String word;
	private final MatchType type;
	private final Object param;

	public Router(int level, String word, Object param) {
		this.level = level;
		this.word = word;
		this.param = param;
		if (word.contains(HttpPathParser.BRACE_LEFT) && word.contains(HttpPathParser.BRACE_RIGHT)) {
			type = MatchType.VARIABLE;
		} else {
			type = MatchType.EXACTLY;
		}
	}

	public boolean isExactly() {
		return type == MatchType.EXACTLY;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Router router = (Router) o;
		if (type == MatchType.EXACTLY) {
			return word.equals(router.word) && level == router.level;
		} else {
			return type.equals(router.type) && level == router.level;
		}
	}

	@Override
	public int hashCode() {
		if (type == MatchType.EXACTLY) {
			return Objects.hash(word, level);
		} else {
			return Objects.hash(type, level);
		}
	}

	public int getLevel() {
		return level;
	}

	public String getWord() {
		return word;
	}

	public MatchType getType() {
		return type;
	}

	public Object getParam() {
		return param;
	}
}
