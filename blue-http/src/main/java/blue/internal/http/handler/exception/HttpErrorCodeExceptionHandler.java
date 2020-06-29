package blue.internal.http.handler.exception;

import blue.http.exception.HttpErrorCodeException;
import blue.internal.http.handler.HandlerResponse;

/**
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class HttpErrorCodeExceptionHandler implements ExceptionHandler
{
	@Override
	public void handle(HandlerResponse response)
	{
		if (!(response.getCause() instanceof HttpErrorCodeException))
			return;

		HttpErrorCodeException ex = (HttpErrorCodeException) response.getCause();
		response.setResult(ex.toJsonString());
		response.setHttpStatus(ex.getHttpStatus());
	}
}
