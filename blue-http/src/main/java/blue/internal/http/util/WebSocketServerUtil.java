package blue.internal.http.util;

import blue.core.common.ErrorCode;
import blue.core.util.JsonUtil;
import blue.http.message.WebSocketResponse;
import blue.internal.http.message.WebSocketBody;
import blue.internal.http.message.WebSocketMessage;
import com.alibaba.fastjson.JSONException;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Jin Zheng
 * @since 2020-02-03
 */
public class WebSocketServerUtil
{
	private static Logger logger = LoggerFactory.getLogger(WebSocketServerUtil.class);

	private WebSocketServerUtil()
	{
	}

	public static void sendText(Channel ch, WebSocketMessage request, WebSocketResponse response)
	{
		WebSocketMessage message = WebSocketMessage.toResponse(request);
		WebSocketBody body = message.getBody();
		body.setCode(response.getCode());
		body.setMessage(response.getMessage());
		body.setData(response.getResult());
		String json = JsonUtil.output(message);
		TextWebSocketFrame frame = new TextWebSocketFrame(json);
		ch.writeAndFlush(frame);
	}

	public static void sendText(Channel ch, WebSocketMessage request, Object result,
	                            ErrorCode errorCode, Object...args)
	{
		WebSocketMessage response = WebSocketMessage.toResponse(request, result, errorCode, args);
		String json = JsonUtil.output(response);
		TextWebSocketFrame frame = new TextWebSocketFrame(json);
		ch.writeAndFlush(frame);
	}

	public static WebSocketMessage getWebSocketMessage(Channel ch, String json)
	{
		WebSocketMessage message = null;
		try
		{
			message = JsonUtil.fromString(json, WebSocketMessage.class);
		}
		catch (JSONException e)
		{
			logger.warn("None JSON: {}", json);
		}
		if (message == null)
		{
			sendText(ch, null, null, ErrorCode.INVALID_JSON);
		}
		return message;
	}

}
