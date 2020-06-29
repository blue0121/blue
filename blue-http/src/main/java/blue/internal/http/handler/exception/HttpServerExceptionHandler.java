package blue.internal.http.handler.exception;

import blue.http.exception.DefaultErrorCode;
import blue.http.exception.ErrorCode;
import blue.http.exception.HttpServerException;
import blue.internal.http.handler.HandlerResponse;

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
		ErrorCode errorCode = DefaultErrorCode.SYS_ERROR;
		response.setResult(errorCode.toJsonString(ex.getMessage()));
		response.setHttpStatus(errorCode.getHttpStatus());
	}

}
