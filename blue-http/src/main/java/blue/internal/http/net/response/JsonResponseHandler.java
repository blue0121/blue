package blue.internal.http.net.response;

import blue.core.common.ErrorCode;
import blue.core.util.JsonUtil;
import blue.http.message.Response;
import blue.internal.http.util.HttpServerUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-14
 */
public class JsonResponseHandler implements ResponseHandler
{
	public JsonResponseHandler()
	{
	}

	@Override
	public boolean accepted(Object target)
	{
		return true;
	}

	@Override
	public void handle(Channel ch, HttpRequest request, Response response)
	{
		String json = null;
		Object target = response.getResult();
		if (target instanceof CharSequence)
		{
			json = target.toString();
		}
		else if (target instanceof JSONObject)
		{
			json = ((JSONObject) target).toJSONString();
		}
		else if (target instanceof ErrorCode)
		{
			json = ((ErrorCode) target).toJsonString();
		}
		else
		{
			json = JsonUtil.output(target);
		}
		response.setResult(json);
		HttpServerUtil.sendText(ch, request, response, true);
	}

}
