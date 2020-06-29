package blue.internal.http.handler.exception;

import blue.internal.http.handler.HandlerResponse;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public interface ExceptionHandler
{
	void handle(HandlerResponse response);
}
