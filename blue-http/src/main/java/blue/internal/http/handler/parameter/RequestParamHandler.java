package blue.internal.http.handler.parameter;

import blue.http.message.Request;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class RequestParamHandler implements ParamHandler<Request>
{
	public RequestParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, Request request)
	{
		return request;
	}
}
