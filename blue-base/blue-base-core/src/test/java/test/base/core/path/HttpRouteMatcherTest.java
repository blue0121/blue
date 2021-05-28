package test.base.core.path;

import blue.base.core.path.RouteMatcher;
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
		while ((index = path.indexOf(RouteMatcher.CHAR_SLASH, start)) != -1) {
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

}
