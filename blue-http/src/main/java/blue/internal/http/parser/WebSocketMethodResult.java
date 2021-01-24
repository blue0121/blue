package blue.internal.http.parser;

import blue.internal.http.annotation.DefaultWebSocketUrlConfig;
import blue.internal.http.annotation.RequestParamConfig;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketMethodResult
{
	private Object target;
	private Method method;
	private List<RequestParamConfig> paramList;

	public WebSocketMethodResult(DefaultWebSocketUrlConfig config)
	{
		this.target = config.getTarget();
		this.method = config.getMethod();
		this.paramList = config.getParamList();
	}

	public Object getTarget()
	{
		return target;
	}

	public Method getMethod()
	{
		return method;
	}

	public List<RequestParamConfig> getParamList()
	{
		return paramList;
	}
}
