package blue.base.internal.core.path.route;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class Router {
	public enum Type {
		EXACTLY,
		WILDCARD,
		MULTI_WILDCARD,
	}

	private final String word;
	private final Type type;
	private final Map<Router, Router> exactly = new HashMap<>();
	private final Map<Router, Router> wildcard = new HashMap<>();

	public Router(String word) {
		this.word = word;
		if (word.contains(HttpRouteMatcher.MULTI_WILDCARD)) {
			type = Type.MULTI_WILDCARD;
		} else if (word.contains(HttpRouteMatcher.WILDCARD)
				|| word.contains(HttpRouteMatcher.BRACE_LEFT)) {
			type = Type.WILDCARD;
		} else {
			type = Type.EXACTLY;
		}
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
		if (type == Type.EXACTLY) {
			return word.equals(router.word);
		} else {
			return type.equals(router.type);
		}
	}

	@Override
	public int hashCode() {
		if (type == Type.EXACTLY) {
			return Objects.hash(word);
		} else {
			return Objects.hash(type);
		}
	}

	public String getWord() {
		return word;
	}

	public Type getType() {
		return type;
	}
}
