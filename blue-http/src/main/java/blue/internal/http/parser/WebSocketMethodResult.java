package blue.internal.http.parser;

import blue.http.annotation.WebSocketUrlConfig;

import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketMethodResult
{
	private Object target;
	private Method method;

	public WebSocketMethodResult(WebSocketUrlConfig config)
	{
		this.target = config.getTarget();
		this.method = config.getMethod();
	}

	public Object getTarget()
	{
		return target;
	}

	public Method getMethod()
	{
		return method;
	}
}
