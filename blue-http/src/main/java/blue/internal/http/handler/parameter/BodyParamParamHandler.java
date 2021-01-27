package blue.internal.http.handler.parameter;

import blue.core.common.ErrorCode;
import blue.http.message.Request;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 2021-01-22
 */
public class BodyParamParamHandler implements ParamHandler<Request>
{
	public BodyParamParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, Request request)
	{
		String param = request.getPost(config.getParamAnnotationValue());
		if (param == null || param.isEmpty())
		{
			if (config.isParamAnnotationRequired())
				throw ErrorCode.REQUIRED.newException(config.getName());

			return null;
		}
		return this.convert(config, param);
	}
}
