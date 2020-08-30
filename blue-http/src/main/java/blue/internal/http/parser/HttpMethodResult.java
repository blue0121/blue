package blue.internal.http.parser;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpUrlConfig;

import java.lang.reflect.Method;
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

	public HttpMethodResult(HttpUrlConfig config, Map<String, String> pathMap)
	{
		this.charset = config.getCharset();
		this.contentType = config.getContentType();
		this.target = config.getTarget();
		this.method = config.getMethod();
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
}
