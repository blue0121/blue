package blue.base.internal.core.path.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class TrieRouter {
	private static Logger logger = LoggerFactory.getLogger(TrieRouter.class);

	private final Map<Router, Set<Router>> exactly = new HashMap<>();
	private final Map<Router, Set<Router>> wildcard = new HashMap<>();

	public TrieRouter() {
	}

	public void add(String pattern, Object param) {
		int index = -1;
		int start = 1;
		int level = 1;
		while ((index = pattern.indexOf('/', start)) != -1) {
			String word = pattern.substring(start, index);
			this.addRouter(level, word, param);
			start = index + 1;
			level++;
		}
		String word = pattern.substring(start);
		this.addRouter(level, word, param);
	}

	private void addRouter(int level, String word, Object param) {
		Router router = new Router(level, word, param);
		Set<Router> set;
		if (router.isExactly()) {
			set = exactly.computeIfAbsent(router, k -> new HashSet<>());
		} else {
			set = wildcard.computeIfAbsent(router, k -> new HashSet<>());
		}
		set.add(router);
	}

	public Map<Router, Set<Router>> getExactly() {
		return Map.copyOf(exactly);
	}

	public Map<Router, Set<Router>> getWildcard() {
		return Map.copyOf(wildcard);
	}
}
