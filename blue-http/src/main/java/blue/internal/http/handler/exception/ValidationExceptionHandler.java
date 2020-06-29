package blue.internal.http.handler.exception;

import blue.http.exception.DefaultErrorCode;
import blue.http.exception.ErrorCode;
import blue.internal.http.handler.HandlerResponse;

import javax.validation.ValidationException;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class ValidationExceptionHandler implements ExceptionHandler
{
	public ValidationExceptionHandler()
	{
	}

	@Override
	public void handle(HandlerResponse response)
	{
		if (!(response.getCause() instanceof ValidationException))
			return;

		ValidationException ex = (ValidationException) response.getCause();
		ErrorCode errorCode = DefaultErrorCode.INVALID_PARAM;
		response.setResult(errorCode.toJsonString(ex.getMessage()));
		response.setHttpStatus(errorCode.getHttpStatus());
	}
}
