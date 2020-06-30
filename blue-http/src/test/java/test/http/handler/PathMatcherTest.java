package test.http.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


/**
 * @author Jin Zheng
 * @since 1.0 2020-01-06
 */
public class PathMatcherTest
{
	private PathMatcher matcher;

	public PathMatcherTest()
	{
	}

	@BeforeEach
	public void before()
	{
		this.matcher = new AntPathMatcher();
	}

	@Test
	public void testMatch()
	{
		Assertions.assertTrue(matcher.match("/{name}", "/test"));
		Assertions.assertTrue(matcher.match("/**", "/test/abc"));
		Assertions.assertTrue(matcher.match("/*/abc", "/test/abc"));
	}

	@Test
	public void testExtractPathWithinPattern()
	{
		System.out.println(matcher.extractPathWithinPattern("/*", "/test"));
		System.out.println(matcher.extractPathWithinPattern("/**", "/test/abc"));
		System.out.println(matcher.extractPathWithinPattern("/*/abc", "/test/abc"));
	}

	@Test
	public void testExtractUriTemplateVariables()
	{
		System.out.println(matcher.extractUriTemplateVariables("/{name}", "/test"));
		System.out.println(matcher.extractUriTemplateVariables("/test/{name}", "/test/abc"));
		System.out.println(matcher.extractUriTemplateVariables("/{name}/abc", "/test/abc"));
	}

	@Test
	public void testCombine()
	{
		Assertions.assertEquals("/test/abc", matcher.combine("/test", "abc"));
		Assertions.assertEquals("/test/abc", matcher.combine("/test", "/abc"));
		Assertions.assertEquals("test/abc", matcher.combine("test", "abc"));
	}

}
