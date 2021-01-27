package blue.internal.http.handler.parameter;

import blue.core.common.ErrorCode;
import blue.http.message.Request;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 2021-01-24
 */
public class PathVariableParamHandler implements ParamHandler<Request>
{
	public PathVariableParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, Request request)
	{
		String param = request.getPathVariable(config.getParamAnnotationValue());
		if (param == null || param.isEmpty())
			throw ErrorCode.REQUIRED.newException(config.getName());

		return this.convert(config, param);
	}
}
