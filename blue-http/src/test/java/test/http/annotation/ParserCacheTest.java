package test.http.annotation;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpMethod;
import blue.internal.http.annotation.DefaultHttpUrlConfig;
import blue.internal.http.annotation.HttpConfigCache;
import blue.internal.http.annotation.HttpUrlKey;
import blue.internal.http.parser.HttpMethodResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class ParserCacheTest
{
	private HttpConfigCache cache = HttpConfigCache.getInstance();

	public ParserCacheTest()
	{
	}

	@BeforeEach
	public void before()
	{
		DefaultHttpUrlConfig config1 = new DefaultHttpUrlConfig();
		config1.setUrl("/hello");
		config1.setHttpMethod(HttpMethod.GET);
		config1.setCharset(Charset.UTF_8);
		config1.setContentType(ContentType.AUTO);
		HttpUrlKey key1 = config1.buildKey();
		cache.put(key1, config1);

		DefaultHttpUrlConfig config2 = new DefaultHttpUrlConfig();
		config2.setUrl("/hello/{id}");
		config2.setHttpMethod(HttpMethod.GET);
		config2.setCharset(Charset.UTF_8);
		config2.setContentType(ContentType.JSON);
		HttpUrlKey key2 = config2.buildKey();
		cache.put(key2, config2);

		DefaultHttpUrlConfig config3 = new DefaultHttpUrlConfig();
		config3.setUrl("/hello/blue");
		config3.setHttpMethod(HttpMethod.GET);
		config3.setCharset(Charset.UTF_8);
		config3.setContentType(ContentType.HTML);
		HttpUrlKey key3 = config3.buildKey();
		cache.put(key3, config3);
	}

	@Test
	public void testGetConfig()
	{
		HttpUrlKey config1 = new HttpUrlKey("/hello", HttpMethod.GET);
		HttpMethodResult result1 = cache.getConfig(config1);
		Assertions.assertNotNull(result1);
		Assertions.assertEquals(ContentType.AUTO, result1.getContentType());

		HttpUrlKey config2 = new HttpUrlKey("/hello/blue", HttpMethod.GET);
		HttpMethodResult result2 = cache.getConfig(config2);
		Assertions.assertNotNull(result2);
		Assertions.assertEquals(ContentType.HTML, result2.getContentType());

		HttpUrlKey config3 = new HttpUrlKey("/hello/2", HttpMethod.GET);
		HttpMethodResult result3 = cache.getConfig(config3);
		Assertions.assertNotNull(result3);
		Assertions.assertEquals(ContentType.JSON, result3.getContentType());

		HttpUrlKey config4 = new HttpUrlKey("/hello/2/3", HttpMethod.GET);
		HttpMethodResult result4 = cache.getConfig(config4);
		Assertions.assertNull(result4);
	}

}
