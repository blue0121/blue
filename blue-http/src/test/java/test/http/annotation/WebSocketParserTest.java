package test.http.annotation;

import blue.internal.http.annotation.WebSocketConfigCache;
import blue.internal.http.annotation.WebSocketUrlKey;
import blue.internal.http.parser.WebSocketMethodResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.http.BaseTest;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketParserTest extends BaseTest
{
	private WebSocketConfigCache parserCache = WebSocketConfigCache.getInstance();

	public WebSocketParserTest()
	{
	}

	@Test
	public void test()
	{
		WebSocketUrlKey topic = new WebSocketUrlKey("/hello", 0);
		WebSocketMethodResult result = parserCache.getConfig(topic);
		Assertions.assertNotNull(result);
		Assertions.assertEquals("index", result.getMethod().getName());
	}

}
