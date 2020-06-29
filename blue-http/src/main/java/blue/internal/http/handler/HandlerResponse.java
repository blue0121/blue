package blue.internal.http.handler;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.message.Response;
import blue.http.message.WebSocketResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Jin Zheng
 * @since 2020-02-06
 */
public class HandlerResponse
{
	private Charset charset;
	private ContentType contentType;
	private Object result;
	private Throwable cause;
	private int code;
	private String message;
	private HttpResponseStatus httpStatus;
	private Object response;

	public HandlerResponse()
	{
	}

	public static HandlerResponse from(Response response)
	{
		if (response == null)
			return null;

		HandlerResponse handlerResponse = new HandlerResponse();
		handlerResponse.setCharset(response.getCharset());
		handlerResponse.setContentType(response.getContentType());
		handlerResponse.setResult(response.getResult());
		handlerResponse.setCause(response.getCause());
		handlerResponse.setResponse(response);
		return handlerResponse;
	}

	public static HandlerResponse from(WebSocketResponse response)
	{
		if (response == null)
			return null;

		HandlerResponse handlerResponse = new HandlerResponse();
		handlerResponse.setResult(response.getResult());
		handlerResponse.setCause(response.getCause());
		handlerResponse.setResponse(response);
		return handlerResponse;
	}

	public Charset getCharset()
	{
		return charset;
	}

	public void setCharset(Charset charset)
	{
		this.charset = charset;
	}

	public ContentType getContentType()
	{
		return contentType;
	}

	public void setContentType(ContentType contentType)
	{
		this.contentType = contentType;
	}

	public Object getResult()
	{
		return result;
	}

	public void setResult(Object result)
	{
		this.result = result;
	}

	public Throwable getCause()
	{
		return cause;
	}

	public void setCause(Throwable cause)
	{
		this.cause = cause;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public HttpResponseStatus getHttpStatus()
	{
		return httpStatus;
	}

	public void setHttpStatus(HttpResponseStatus httpStatus)
	{
		this.httpStatus = httpStatus;
	}

	public Object getResponse()
	{
		return response;
	}

	public void setResponse(Object response)
	{
		this.response = response;
	}
}
