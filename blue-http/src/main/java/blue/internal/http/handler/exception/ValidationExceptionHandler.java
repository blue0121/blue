package blue.internal.http.handler.exception;

import blue.core.common.ErrorCode;
import blue.internal.http.handler.HandlerResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

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
		ErrorCode errorCode = ErrorCode.INVALID_PARAM;
		response.setResult(errorCode.toJsonString(ex.getMessage()));
		response.setHttpStatus(HttpResponseStatus.valueOf(errorCode.getHttpStatus()));
	}
}
