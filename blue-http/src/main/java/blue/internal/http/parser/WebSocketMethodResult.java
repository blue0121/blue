package blue.internal.http.parser;

import blue.core.reflect.BeanMethod;
import blue.core.reflect.JavaBean;
import blue.internal.http.annotation.DefaultWebSocketUrlConfig;
import blue.internal.http.annotation.RequestParamConfig;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketMethodResult
{
	private JavaBean javaBean;
	private BeanMethod method;
	private List<RequestParamConfig> paramList;

	public WebSocketMethodResult(DefaultWebSocketUrlConfig config)
	{
		this.javaBean = config.getJavaBean();
		this.method = config.getMethod();
		this.paramList = config.getParamList();
	}

	public JavaBean getJavaBean()
	{
		return javaBean;
	}

	public BeanMethod getMethod()
	{
		return method;
	}

	public List<RequestParamConfig> getParamList()
	{
		return paramList;
	}
}
