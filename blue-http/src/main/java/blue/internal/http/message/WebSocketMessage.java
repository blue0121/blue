package blue.internal.http.message;

import blue.http.exception.ErrorCode;

/**
 * @author Jin Zheng
 * @since 2020-01-29
 */
public class WebSocketMessage
{
	private WebSocketHeader header;
	private WebSocketBody body;

	public WebSocketMessage()
	{
	}

	public WebSocketMessage(WebSocketHeader header, WebSocketBody body)
	{
		this.header = header;
		this.body = body;
	}

	public static WebSocketMessage toResponse(WebSocketMessage request)
	{
		WebSocketHeader header = WebSocketHeader.from(request != null ? request.getHeader() : null);
		WebSocketBody body = new WebSocketBody();
		WebSocketMessage response = new WebSocketMessage(header, body);
		return response;
	}

	public static WebSocketMessage toResponse(WebSocketMessage request, Object result,
	                                          ErrorCode errorCode, Object...args)
	{
		WebSocketMessage response = WebSocketMessage.toResponse(request);
		WebSocketBody body = response.getBody();
		body.setData(request);
		body.setCode(errorCode.getCode());
		body.setMessage(errorCode.getMessage(args));
		return response;
	}

	public WebSocketHeader getHeader()
	{
		return header;
	}

	public void setHeader(WebSocketHeader header)
	{
		this.header = header;
	}

	public WebSocketBody getBody()
	{
		return body;
	}

	public void setBody(WebSocketBody body)
	{
		this.body = body;
	}
}
