package blue.http.exception;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketServerException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public WebSocketServerException(String message)
	{
		super(message);
	}

	public WebSocketServerException(Throwable cause)
	{
		super(cause);
	}

}
