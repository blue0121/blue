package blue.jms;

import blue.core.message.MessageException;

/**
 * @author Jin Zheng
 * @since 2019-08-02
 */
public class JmsException extends MessageException
{
	private static final long serialVersionUID = 1L;

	public JmsException(String message)
	{
		super(message);
	}

	public JmsException(Throwable e)
	{
		super(e);
	}

}
