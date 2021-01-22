package blue.internal.http.handler.parameter;

import blue.core.util.JsonUtil;
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
		String content = request.getContent();
		if (content == null || content.isEmpty())
			return null;

		Object target = JsonUtil.fromString(content, config.getParamClazz());
		this.valid(config, target);
		return target;
	}
}
