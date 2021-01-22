package blue.internal.http.handler.parameter;

import blue.http.message.Request;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class StringParamHandler implements ParamHandler<Request>
{
	public StringParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, Request request)
	{
		String content = request.getContent();
		this.valid(config, content);
		return content;
	}
}
