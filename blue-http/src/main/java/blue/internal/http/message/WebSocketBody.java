package blue.internal.http.message;

/**
 * @author Jin Zheng
 * @since 2020-01-29
 */
public class WebSocketBody
{
	private int code = 0; // 0:成功
	private String message;
	private Object data;

	public WebSocketBody()
	{
	}

	public WebSocketBody(int code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public WebSocketBody(Object data)
	{
		this.data = data;
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

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}
}
