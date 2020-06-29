package blue.internal.http.message;

import blue.http.message.WebSocketResponse;

/**
 * @author Jin Zheng
 * @since 2020-02-05
 */
public class DefaultWebSocketResponse implements WebSocketResponse
{
	private int code;
	private String message;
	private Object result;
	private Throwable cause;

	public DefaultWebSocketResponse()
	{
	}

	@Override
	public int getCode()
	{
		return code;
	}

	@Override
	public String getMessage()
	{
		return message;
	}

	@Override
	public Object getResult()
	{
		return result;
	}

	@Override
	public void setResult(Object result)
	{
		this.result = result;
	}

	@Override
	public Throwable getCause()
	{
		return cause;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setCause(Throwable cause)
	{
		this.cause = cause;
	}
}
