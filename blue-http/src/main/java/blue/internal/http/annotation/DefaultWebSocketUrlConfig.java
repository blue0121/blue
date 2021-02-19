package blue.internal.http.annotation;

import blue.core.reflect.BeanMethod;
import blue.core.reflect.JavaBean;
import blue.http.annotation.WebSocketUrlConfig;

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
	private JavaBean javaBean;
	private BeanMethod method;
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

	public void setJavaBean(JavaBean javaBean)
	{
		this.javaBean = javaBean;
	}

	public void setMethod(BeanMethod method)
	{
		this.method = method;
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
	public BeanMethod getMethod()
	{
		return method;
	}

	@Override
	public JavaBean getJavaBean()
	{
		return javaBean;
	}
}
