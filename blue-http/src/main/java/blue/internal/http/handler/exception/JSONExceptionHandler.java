package blue.internal.http.handler.exception;

import blue.core.common.ErrorCode;
import blue.internal.http.handler.HandlerResponse;
import com.alibaba.fastjson.JSONException;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class JSONExceptionHandler implements ExceptionHandler
{
	public JSONExceptionHandler()
	{
	}

	@Override
	public void handle(HandlerResponse response)
	{
		if (!(response.getCause() instanceof JSONException))
			return;

		ErrorCode errorCode = ErrorCode.INVALID_JSON;
		response.setResult(errorCode.toJsonString());
		response.setHttpStatus(HttpResponseStatus.valueOf(errorCode.getHttpStatus()));
	}
}
