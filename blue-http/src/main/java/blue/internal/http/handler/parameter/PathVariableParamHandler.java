package blue.internal.http.handler.parameter;

import blue.core.common.ErrorCode;
import blue.http.annotation.PathVariable;
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
		PathVariable annotation = (PathVariable) config.getParamAnnotation();
		String param = request.getPathVariable(annotation.value());
		if (param == null || param.isEmpty())
			throw ErrorCode.REQUIRED.newException(config.getName());

		return this.convert(config, param);
	}
}
