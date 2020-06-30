package test.http.annotation;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpMethod;
import blue.internal.http.parser.HttpMethodResult;
import blue.internal.http.parser.HttpUrlConfig;
import blue.internal.http.parser.ParserCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.http.BaseTest;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class HttpParserTest extends BaseTest
{
	private ParserCache parserCache = ParserCache.getInstance();

	public HttpParserTest()
	{
	}

	@Test
	public void test()
	{
		HttpUrlConfig config = new HttpUrlConfig("/hello", HttpMethod.POST);
		HttpMethodResult result = parserCache.getConfig(config);
		System.out.println(result);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(Charset.UTF_8, result.getCharset());
		Assertions.assertEquals(ContentType.AUTO, result.getContentType());

		Assertions.assertTrue(parserCache.containsConfig(new HttpUrlConfig("/echo", HttpMethod.GET)));
		Assertions.assertTrue(parserCache.containsConfig(new HttpUrlConfig("/echo", HttpMethod.POST)));
		Assertions.assertTrue(parserCache.containsConfig(new HttpUrlConfig("/echo", HttpMethod.DELETE)));
		Assertions.assertTrue(parserCache.containsConfig(new HttpUrlConfig("/echo", HttpMethod.PUT)));
	}

}
