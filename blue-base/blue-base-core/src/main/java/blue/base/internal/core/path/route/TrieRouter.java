package blue.base.internal.core.path.route;

import blue.base.core.path.RouteMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class TrieRouter {
	private static Logger logger = LoggerFactory.getLogger(TrieRouter.class);

	private final Map<Router, Router> exactly = new HashMap<>();
	private final Map<Router, Router> wildcard = new HashMap<>();

	public TrieRouter() {
	}

	public void add(String pattern, Object param) {
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
	}

	private void addRouter(String word, Object param) {
		Router r = new Router(word);
		if (r.getType() == Router.Type.EXACTLY) {

		} else {

		}
	}

	private void addRouter(Map<Router, Router> map, String word, Object param) {

	}

}
