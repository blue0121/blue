package blue.internal.http.handler.exception;

import blue.core.common.ErrorCodeException;
import blue.internal.http.handler.HandlerResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class ErrorCodeExceptionHandler implements ExceptionHandler
{
	@Override
	public void handle(HandlerResponse response)
	{
		if (!(response.getCause() instanceof ErrorCodeException))
			return;

		ErrorCodeException ex = (ErrorCodeException) response.getCause();
		response.setResult(ex.toJsonString());
		response.setHttpStatus(HttpResponseStatus.valueOf(ex.getHttpStatus()));
	}
}
