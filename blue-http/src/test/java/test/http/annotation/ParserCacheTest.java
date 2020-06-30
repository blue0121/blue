package test.http.annotation;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpMethod;
import blue.internal.http.parser.HttpMethodResult;
import blue.internal.http.parser.HttpUrlConfig;
import blue.internal.http.parser.HttpUrlMethod;
import blue.internal.http.parser.ParserCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class ParserCacheTest
{
	private ParserCache cache = ParserCache.getInstance();

	public ParserCacheTest()
	{
	}

	@BeforeEach
	public void before()
	{
		HttpUrlConfig config1 = new HttpUrlConfig("/hello", HttpMethod.GET);
		HttpUrlMethod method1 = new HttpUrlMethod(Charset.UTF_8, null, ContentType.AUTO);
		cache.putConfig(config1, method1);

		HttpUrlConfig config2 = new HttpUrlConfig("/hello/{id}", HttpMethod.GET);
		HttpUrlMethod method2 = new HttpUrlMethod(Charset.UTF_8, null, ContentType.JSON);
		cache.putConfig(config2, method2);

		HttpUrlConfig config3 = new HttpUrlConfig("/hello/blue", HttpMethod.GET);
		HttpUrlMethod method3 = new HttpUrlMethod(Charset.UTF_8, null, ContentType.HTML);
		cache.putConfig(config3, method3);
	}

	@Test
	public void testGetConfig()
	{
		HttpUrlConfig config1 = new HttpUrlConfig("/hello", HttpMethod.GET);
		HttpMethodResult result1 = cache.getConfig(config1);
		Assertions.assertNotNull(result1);
		Assertions.assertEquals(ContentType.AUTO, result1.getContentType());

		HttpUrlConfig config2 = new HttpUrlConfig("/hello/blue", HttpMethod.GET);
		HttpMethodResult result2 = cache.getConfig(config2);
		Assertions.assertNotNull(result2);
		Assertions.assertEquals(ContentType.HTML, result2.getContentType());

		HttpUrlConfig config3 = new HttpUrlConfig("/hello/2", HttpMethod.GET);
		HttpMethodResult result3 = cache.getConfig(config3);
		Assertions.assertNotNull(result3);
		Assertions.assertEquals(ContentType.JSON, result3.getContentType());

		HttpUrlConfig config4 = new HttpUrlConfig("/hello/2/3", HttpMethod.GET);
		HttpMethodResult result4 = cache.getConfig(config4);
		Assertions.assertNull(result4);
	}

}
