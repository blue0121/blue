package blue.internal.http.annotation;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpMethod;
import blue.http.annotation.HttpUrlConfig;

import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public class DefaultHttpUrlConfig implements HttpUrlConfig
{
	private String name;
	private String url;
	private HttpMethod httpMethod;
	private Charset charset;
	private ContentType contentType;
	private Method method;

	public DefaultHttpUrlConfig()
	{
	}

	public HttpUrlKey buildKey()
	{
		return new HttpUrlKey(url, httpMethod);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public void setHttpMethod(HttpMethod httpMethod)
	{
		this.httpMethod = httpMethod;
	}

	public void setCharset(Charset charset)
	{
		this.charset = charset;
	}

	public void setContentType(ContentType contentType)
	{
		this.contentType = contentType;
	}

	public void setMethod(Method method)
	{
		this.method = method;
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
	public HttpMethod getHttpMethod()
	{
		return httpMethod;
	}

	@Override
	public Charset getCharset()
	{
		return charset;
	}

	@Override
	public ContentType getContentType()
	{
		return contentType;
	}

	@Override
	public Method getMethod()
	{
		return method;
	}
}
