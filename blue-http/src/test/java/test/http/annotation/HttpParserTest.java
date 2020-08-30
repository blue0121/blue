package test.http.annotation;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpMethod;
import blue.internal.http.annotation.HttpConfigCache;
import blue.internal.http.annotation.HttpUrlKey;
import blue.internal.http.parser.HttpMethodResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.http.BaseTest;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class HttpParserTest extends BaseTest
{
	private HttpConfigCache parserCache = HttpConfigCache.getInstance();

	public HttpParserTest()
	{
	}

	@Test
	public void test()
	{
		HttpUrlKey key = new HttpUrlKey("/hello", HttpMethod.POST);
		HttpMethodResult result = parserCache.getConfig(key);
		System.out.println(result);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(Charset.UTF_8, result.getCharset());
		Assertions.assertEquals(ContentType.AUTO, result.getContentType());

		Assertions.assertTrue(parserCache.contains(new HttpUrlKey("/echo", HttpMethod.GET)));
		Assertions.assertTrue(parserCache.contains(new HttpUrlKey("/echo", HttpMethod.POST)));
		Assertions.assertTrue(parserCache.contains(new HttpUrlKey("/echo", HttpMethod.DELETE)));
		Assertions.assertTrue(parserCache.contains(new HttpUrlKey("/echo", HttpMethod.PUT)));
	}

}
