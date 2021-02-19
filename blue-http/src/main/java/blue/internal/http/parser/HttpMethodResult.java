package blue.internal.http.parser;

import blue.core.reflect.BeanMethod;
import blue.core.reflect.JavaBean;
import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.internal.http.annotation.DefaultHttpUrlConfig;
import blue.internal.http.annotation.RequestParamConfig;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-21
 */
public class HttpMethodResult
{
	private Charset charset;
	private ContentType contentType;
	private Map<String, String> pathMap;
	private JavaBean javaBean;
	private BeanMethod method;
	private List<RequestParamConfig> paramList;

	public HttpMethodResult(DefaultHttpUrlConfig config, Map<String, String> pathMap)
	{
		this.charset = config.getCharset();
		this.contentType = config.getContentType();
		this.javaBean = config.getJavaBean();
		this.method = config.getMethod();
		this.paramList = config.getParamList();
		if (pathMap == null)
		{
			this.pathMap = Map.of();
		}
		else
		{
			this.pathMap = Map.copyOf(pathMap);
		}
	}

	public Charset getCharset()
	{
		return charset;
	}

	public ContentType getContentType()
	{
		return contentType;
	}

	public Map<String, String> getPathMap()
	{
		return pathMap;
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
