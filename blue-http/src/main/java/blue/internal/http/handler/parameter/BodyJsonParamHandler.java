package blue.internal.http.handler.parameter;

import blue.core.common.ErrorCode;
import blue.core.util.JsonUtil;
import blue.http.annotation.BodyJson;
import blue.http.message.Request;
import blue.internal.http.annotation.RequestParamConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class BodyJsonParamHandler implements ParamHandler<Request>
{
	public BodyJsonParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, Request request)
	{
		BodyJson annotation = (BodyJson) config.getParamAnnotation();
		String body = request.getContent();
		if (body == null || body.isEmpty())
		{
			if (annotation.required())
				throw ErrorCode.REQUIRED.newException(config.getName());

			return null;
		}
		if (annotation.jsonPath().isEmpty())
			return JsonUtil.fromString(body, config.getParamClazz());

		JSONObject object = JSON.parseObject(body);
		Object target = JSONPath.eval(object, annotation.jsonPath());
		if (target instanceof JSONObject)
		{
			Object subTarget = ((JSONObject) target).toJavaObject(config.getParamClazz());
			this.valid(config, subTarget);
			return subTarget;
		}
		return target;
	}
}
