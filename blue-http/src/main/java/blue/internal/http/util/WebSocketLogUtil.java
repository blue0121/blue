package blue.internal.http.util;

import blue.http.exception.ErrorCode;
import blue.http.exception.HttpErrorCodeException;
import blue.http.exception.HttpServerException;
import blue.http.message.WebSocketRequest;
import blue.internal.http.handler.HandlerResponse;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2020-02-07
 */
public class WebSocketLogUtil
{
	private static Logger logger = LoggerFactory.getLogger(WebSocketLogUtil.class);

	private WebSocketLogUtil()
	{
	}


	public static void error(WebSocketRequest request, HandlerResponse response)
	{


		Throwable cause = response.getCause();
		if (cause != null)
		{
			if (cause.getClass() == HttpServerException.class || cause.getClass() == HttpErrorCodeException.class)
			{
				StackTraceElement[] elements = cause.getStackTrace();
				StackTraceElement element = elements.length > 0 ? elements[0] : null;
				logger.error("EXCEPTION ({}): {}, at {}", cause.getClass().getSimpleName(), cause.getMessage(), element);
			}
			else
			{
				logger.error("EXCEPTION: ", cause);
			}
		}
	}

	private static void log(WebSocketRequest request, HandlerResponse response)
	{
		StringBuilder sb = new StringBuilder(128);
		sb.append("url: ").append(request.getUrl());


		JSONObject content = new JSONObject();
		content.put(ErrorCode.CODE, response.getCode());
		content.put(ErrorCode.MESSAGE, response.getMessage());
		content.put("data", response.getResult());
		sb.append("\nRETURN: ").append(content.toJSONString());
		logger.info(sb.toString());
	}

}
