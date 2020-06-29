package blue.internal.http.handler.parameter;

import blue.http.message.Request;

/**
 * @author Jin Zheng
 * @since 2020-01-11
 */
public class StringParameterHandler implements ParameterHandler<Request>
{
	public StringParameterHandler()
	{
	}

	@Override
	public boolean accepted(Object target)
	{
		return target != null && target instanceof Request;
	}

	@Override
	public Object handleParameter(Request request)
	{
		return request.getContent();
	}
}
