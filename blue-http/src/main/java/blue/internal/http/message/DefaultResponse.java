package blue.internal.http.message;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.message.Response;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-03
 */
public class DefaultResponse implements Response
{

	private Object result;
	private Throwable cause;
	private Charset charset;
	private ContentType contentType;
	private HttpResponseStatus httpStatus;
	private Map<String, String> cookieMap = new HashMap<>();
	private Map<String, Object> paramMap = new HashMap<>();

	public DefaultResponse()
	{
	}

	@Override
	public void setResult(Object result)
	{
		this.result = result;
	}

	@Override
	public Object getResult()
	{
		return result;
	}

	public void setCharset(Charset charset)
	{
		this.charset = charset;
	}

	public void setContentType(ContentType contentType)
	{
		this.contentType = contentType;
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
	public HttpResponseStatus getHttpStatus()
	{
		return httpStatus;
	}

	@Override
	public void setHttpStatus(HttpResponseStatus httpStatus)
	{
		this.httpStatus = httpStatus;
	}

	@Override
	public Throwable getCause()
	{
		return cause;
	}

	public void setCause(Throwable cause)
	{
		this.cause = cause;
	}

	@Override
	public Map<String, String> getCookie()
	{
		return cookieMap;
	}

	@Override
	public Map<String, Object> getParam()
	{
		return paramMap;
	}


}
