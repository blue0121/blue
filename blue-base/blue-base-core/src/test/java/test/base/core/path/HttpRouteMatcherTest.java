package test.base.core.path;

import blue.base.core.path.MatchResult;
import blue.base.internal.core.path.route.HttpRouteMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-28
 */
public class HttpRouteMatcherTest {
	public HttpRouteMatcherTest() {
	}

	@Test
	public void testSubstring() {
		String path = "/a/b/c";
		int index = -1;
		int start = 1;
		List<String> list = new ArrayList<>();
		while ((index = path.indexOf('/', start)) != -1) {
			String word = path.substring(start, index);
			System.out.println(word);
			list.add(word);
			start = index + 1;
		}
		String word = path.substring(start);
		list.add(word);
		Assertions.assertEquals(3, list.size());
		Assertions.assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	@Test
	public void testExactly() {
		HttpRouteMatcher routeMatcher = new HttpRouteMatcher();
		String url = "/test/123";
		Object param = 123;
		routeMatcher.add(url, param);
		MatchResult result = routeMatcher.match(url);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(url, result.getRoute());
		Assertions.assertEquals(url, result.getPattern());
		Assertions.assertEquals(param, result.getParam());
		Assertions.assertTrue(result.getVars().isEmpty());
	}

}
