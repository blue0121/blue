package blue.internal.http.handler.parameter;

import blue.http.message.Request;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class BodyContentParamHandler implements ParamHandler<Request>
{
	public BodyContentParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, Request request)
	{
		String body = request.getContent();
		if (body == null || body.isEmpty())
			return null;

		Object target = this.convert(config, body);
		return target;
	}
}
