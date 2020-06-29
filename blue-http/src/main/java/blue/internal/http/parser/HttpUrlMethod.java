package blue.internal.http.parser;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;

import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class HttpUrlMethod
{
	private Charset charset;
	private Method method;
	private ContentType contentType;

	public HttpUrlMethod()
	{
	}

	public HttpUrlMethod(Charset charset, Method method, ContentType contentType)
	{
		this.charset = charset;
		this.method = method;
		this.contentType = contentType;
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
}
