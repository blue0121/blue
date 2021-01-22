package blue.internal.http.handler.parameter;

import blue.http.message.WebSocketRequest;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class WebSocketRequestParamHandler implements ParamHandler<WebSocketRequest>
{
	public WebSocketRequestParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, WebSocketRequest request)
	{
		return request;
	}
}
