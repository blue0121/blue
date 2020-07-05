package blue.core.message;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public class MessageException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public MessageException(String message)
	{
		super(message);
	}

	public MessageException(Throwable e)
	{
		super(e);
	}

}
