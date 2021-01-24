package blue.internal.http.annotation;

import blue.http.annotation.WebSocketUrlConfig;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public class DefaultWebSocketUrlConfig implements WebSocketUrlConfig
{
	private String name;
	private String url;
	private int version;
	private Object target;
	private Method method;
	private List<RequestParamConfig> paramList;

	public DefaultWebSocketUrlConfig()
	{
	}

	public WebSocketUrlKey buildKey()
	{
		return new WebSocketUrlKey(url, version);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public void setMethod(Method method)
	{
		this.method = method;
	}

	public void setTarget(Object target)
	{
		this.target = target;
	}

	public List<RequestParamConfig> getParamList()
	{
		return paramList;
	}

	public void setParamList(List<RequestParamConfig> paramList)
	{
		this.paramList = List.copyOf(paramList);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getUrl()
	{
		return url;
	}

	@Override
	public int getVersion()
	{
		return version;
	}

	@Override
	public Method getMethod()
	{
		return method;
	}

	@Override
	public Object getTarget()
	{
		return target;
	}
}
