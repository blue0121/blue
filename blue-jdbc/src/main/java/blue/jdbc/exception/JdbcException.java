package blue.jdbc.exception;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-22
 */
public class JdbcException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public JdbcException(String message)
	{
		super(message);
	}

	public JdbcException(Throwable throwable)
	{
		super(throwable);
	}

}
