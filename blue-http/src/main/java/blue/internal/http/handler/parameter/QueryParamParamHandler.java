package blue.internal.http.handler.parameter;

import blue.core.common.ErrorCode;
import blue.http.annotation.BodyParam;
import blue.http.message.Request;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 2021-01-22
 */
public class QueryParamParamHandler implements ParamHandler<Request>
{
	public QueryParamParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, Request request)
	{
		BodyParam annotation = (BodyParam) config.getParamAnnotation();
		String param = request.getQueryString(annotation.value());
		if (param == null || param.isEmpty())
		{
			if (annotation.required())
				throw ErrorCode.REQUIRED.newException(config.getName());

			return null;
		}
		return this.convert(config, param);
	}
}
