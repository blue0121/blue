package blue.internal.http.handler.exception;

import blue.core.common.ErrorCode;
import blue.internal.http.handler.HandlerResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
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

		ErrorCode errorCode = ErrorCode.NOT_FOUND;
		response.setResult(errorCode.toJsonString());
		response.setHttpStatus(HttpResponseStatus.valueOf(errorCode.getHttpStatus()));
	}
}
