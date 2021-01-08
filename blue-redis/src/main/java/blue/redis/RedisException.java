package blue.redis;

import blue.core.message.MessageException;

/**
 * @author Jin Zheng
 * @since 2019-10-30
 */
public class RedisException extends MessageException
{
	private static final long serialVersionUID = 1L;

	public RedisException(String message)
	{
		super(message);
	}

	public RedisException(Throwable e)
	{
		super(e);
	}
}
