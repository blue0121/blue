package blue.internal.http.parser;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-21
 */
public class HttpMethodResult
{
	private Charset charset;
	private Method method;
	private ContentType contentType;
	private Map<String, String> pathMap;

	public HttpMethodResult()
	{
	}

	public HttpMethodResult(HttpUrlMethod method, Map<String, String> pathMap)
	{
		this.charset = method.getCharset();
		this.method = method.getMethod();
		this.contentType = method.getContentType();
		if (pathMap == null)
		{
			this.pathMap = new HashMap<>();
		}
		else
		{
			this.pathMap = new HashMap<>(pathMap);
		}
	}

	public Charset getCharset()
	{
		return charset;
	}

	public void setCharset(Charset charset)
	{
		this.charset = charset;
	}

	public Method getMethod()
	{
		return method;
	}

	public void setMethod(Method method)
	{
		this.method = method;
	}

	public ContentType getContentType()
	{
		return contentType;
	}

	public void setContentType(ContentType contentType)
	{
		this.contentType = contentType;
	}

	public Map<String, String> getPathMap()
	{
		return pathMap;
	}

	public void setPathMap(Map<String, String> pathMap)
	{
		this.pathMap = pathMap;
	}
}
