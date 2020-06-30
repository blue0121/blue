package blue.internal.http.net.response;

import blue.http.exception.DefaultErrorCode;
import blue.http.exception.ErrorCode;
import blue.http.message.Response;
import blue.internal.http.util.HttpServerUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;

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
		ErrorCode errorCode = DefaultErrorCode.SUCCESS;
		response.setResult(errorCode.toJsonString());
		response.setHttpStatus(errorCode.getHttpStatus());
		HttpServerUtil.sendText(ch, request, response, true);
	}
}
