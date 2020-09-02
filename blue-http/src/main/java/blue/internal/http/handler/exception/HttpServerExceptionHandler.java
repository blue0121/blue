package blue.internal.http.handler.exception;

import blue.core.common.ErrorCode;
import blue.http.exception.HttpServerException;
import blue.internal.http.handler.HandlerResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class HttpServerExceptionHandler implements ExceptionHandler
{
	public HttpServerExceptionHandler()
	{
	}

	@Override
	public void handle(HandlerResponse response)
	{
		if (!(response.getCause() instanceof HttpServerException))
			return;

		HttpServerException ex = (HttpServerException) response.getCause();
		ErrorCode errorCode = ErrorCode.SYS_ERROR;
		response.setResult(errorCode.toJsonString(ex.getMessage()));
		response.setHttpStatus(HttpResponseStatus.valueOf(errorCode.getHttpStatus()));
	}

}
