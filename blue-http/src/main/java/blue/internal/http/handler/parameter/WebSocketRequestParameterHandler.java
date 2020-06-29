package blue.internal.http.handler.parameter;

import blue.http.message.WebSocketRequest;

/**
 * @author Jin Zheng
 * @since 2020-02-06
 */
public class WebSocketRequestParameterHandler implements ParameterHandler<WebSocketRequest>
{
	public WebSocketRequestParameterHandler()
	{
	}

	@Override
	public boolean accepted(Object target)
	{
		return target != null && target instanceof WebSocketRequest;
	}

	@Override
	public Object handleParameter(WebSocketRequest request)
	{
		return request;
	}
}
