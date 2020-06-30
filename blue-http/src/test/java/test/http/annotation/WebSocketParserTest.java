package test.http.annotation;

import blue.internal.http.parser.ParserCache;
import blue.internal.http.parser.WebSocketMethodResult;
import blue.internal.http.parser.WebSocketUrlConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.http.BaseTest;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketParserTest extends BaseTest
{
	private ParserCache parserCache = ParserCache.getInstance();

	public WebSocketParserTest()
	{
	}

	@Test
	public void test()
	{
		WebSocketUrlConfig topic = new WebSocketUrlConfig("/hello", 0);
		WebSocketMethodResult result = parserCache.getConfig(topic);
		Assertions.assertNotNull(result);
		Assertions.assertEquals("index", result.getMethod().getName());
	}

}
