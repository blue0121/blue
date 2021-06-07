package test.base.core.path;

import blue.base.internal.core.path.route.Router;
import blue.base.internal.core.path.route.TrieRouter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2021-06-01
 */
public class TrieRouterTest {
	public TrieRouterTest() {
	}

	@Test
	public void testAdd1() {
		TrieRouter trieRouter = new TrieRouter();
		trieRouter.add("/test/{id}", null);
		var exactly = trieRouter.getExactly();
		Assertions.assertEquals(1, exactly.size());
		var exactlySet = exactly.get(new Router(1, "test", null));
		Assertions.assertEquals(1, exactlySet.size());
		Assertions.assertTrue(exactlySet.contains(new Router(1, "test", null)));

		var wildcard = trieRouter.getWildcard();
		Assertions.assertEquals(1, wildcard.size());
		var wildcardSet = wildcard.get(new Router(2, "{id}", null));
		Assertions.assertEquals(1, wildcardSet.size());
		Assertions.assertTrue(wildcardSet.contains(new Router(2, "{id}", null)));

	}
}
