package test.http.message;

import blue.core.util.JsonUtil;
import blue.core.util.StringUtil;
import blue.internal.http.message.WebSocketBody;
import blue.internal.http.message.WebSocketHeader;
import blue.internal.http.message.WebSocketMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2020-02-07
 */
public class WebSocketMessageTest
{
	public WebSocketMessageTest()
	{
	}

	@Test
	public void test1()
	{
		String json = StringUtil.getString(WebSocketMessageTest.class, "/json/websocket-1.json");
		System.out.println(json);
		WebSocketMessage message = JsonUtil.fromString(json, WebSocketMessage.class);
		Assertions.assertNotNull(message);
		Assertions.assertNotNull(message.getHeader());
		Assertions.assertNotNull(message.getBody());

		WebSocketHeader header = message.getHeader();
		Assertions.assertEquals(1L, header.getMessageId());
		Assertions.assertEquals(1L, header.getTimestamp());
		Assertions.assertEquals(1, header.getType());
		Assertions.assertEquals("/hello", header.getUrl());
		Assertions.assertEquals(0, header.getVersion());

		WebSocketBody body = message.getBody();
		Assertions.assertEquals(0, body.getCode());
		Assertions.assertNull(body.getMessage());
		Assertions.assertEquals("blue", body.getData());
	}

}
