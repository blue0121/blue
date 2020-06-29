package blue.internal.http.handler.exception;

import blue.http.exception.DefaultErrorCode;
import blue.http.exception.ErrorCode;
import blue.internal.http.handler.HandlerResponse;
import org.springframework.beans.BeansException;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class BeansExceptionHandler implements ExceptionHandler
{
	public BeansExceptionHandler()
	{
	}

	@Override
	public void handle(HandlerResponse response)
	{
		if (!(response.getCause() instanceof BeansException))
			return;

		ErrorCode errorCode = DefaultErrorCode.NOT_FOUND;
		response.setResult(errorCode.toJsonString());
		response.setHttpStatus(errorCode.getHttpStatus());
	}
}
