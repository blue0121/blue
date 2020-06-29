package blue.internal.http.parser;

import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketMethod
{
	private Method method;

	public WebSocketMethod()
	{
	}

	public WebSocketMethod(Method method)
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
