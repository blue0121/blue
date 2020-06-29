package blue.internal.http.handler.exception;

import blue.http.exception.DefaultErrorCode;
import blue.http.exception.ErrorCode;
import blue.internal.http.handler.HandlerResponse;
import com.alibaba.fastjson.JSONException;

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

		ErrorCode errorCode = DefaultErrorCode.INVALID_JSON;
		response.setResult(errorCode.toJsonString());
		response.setHttpStatus(errorCode.getHttpStatus());
	}
}
