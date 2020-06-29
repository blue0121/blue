package blue.internal.http.parser;

import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketMethodResult
{
	private Method method;

	public WebSocketMethodResult()
	{
	}

	public WebSocketMethodResult(Method method)
	{
		this.method = method;
	}

	public Method getMethod()
	{
		return method;
	}

	public void setMethod(Method method)
	{
		this.method = method;
	}
}
