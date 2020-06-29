package blue.http.exception;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class HttpServerException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public HttpServerException(String message)
	{
		super(message);
	}

	public HttpServerException(Throwable cause)
	{
		super(cause);
	}

}
