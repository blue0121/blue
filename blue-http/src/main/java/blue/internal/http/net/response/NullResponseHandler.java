package blue.internal.http.net.response;

import blue.core.common.ErrorCode;
import blue.http.message.Response;
import blue.internal.http.util.HttpServerUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class NullResponseHandler implements ResponseHandler
{
	public NullResponseHandler()
	{
	}

	@Override
	public boolean accepted(Object target)
	{
		return target == null;
	}

	@Override
	public void handle(Channel ch, HttpRequest request, Response response)
	{
		ErrorCode errorCode = ErrorCode.SUCCESS;
		response.setResult(errorCode.toJsonString());
		response.setHttpStatus(HttpResponseStatus.valueOf(errorCode.getHttpStatus()));
		HttpServerUtil.sendText(ch, request, response, true);
	}
}
