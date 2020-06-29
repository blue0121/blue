package blue.http.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class HttpErrorCodeException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private ErrorCode errorCode;
	private Object[] args;
	private String message;

	public HttpErrorCodeException(ErrorCode errorCode, Object...args)
	{
		this.errorCode = errorCode;
		this.args = args;
		this.message = errorCode.getMessage(args);
	}

	public ErrorCode getErrorCode()
	{
		return errorCode;
	}

	public int getCode()
	{
		return errorCode.getCode();
	}

	public HttpResponseStatus getHttpStatus()
	{
		return errorCode.getHttpStatus();
	}

	public String toJsonString()
	{
		return errorCode.toJsonString(args);
	}

	@Override
	public String getMessage()
	{
		return message;
	}

	@Override
	public String toString()
	{
		return this.getClass().getName() + ": " + message;
	}
}
