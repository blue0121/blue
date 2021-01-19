package blue.internal.http.parser;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.internal.http.annotation.DefaultHttpUrlConfig;
import blue.internal.http.annotation.HttpUrlParamConfig;

import java.lang.reflect.Method;
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
	private Object target;
	private Method method;
	private List<HttpUrlParamConfig> paramList;

	public HttpMethodResult(DefaultHttpUrlConfig config, Map<String, String> pathMap)
	{
		this.charset = config.getCharset();
		this.contentType = config.getContentType();
		this.target = config.getTarget();
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

	public Object getTarget()
	{
		return target;
	}

	public Method getMethod()
	{
		return method;
	}

	public List<HttpUrlParamConfig> getParamList()
	{
		return paramList;
	}
}
