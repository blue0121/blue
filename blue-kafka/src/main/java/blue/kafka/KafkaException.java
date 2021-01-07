package blue.kafka;

import blue.core.message.MessageException;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-12
 */
public class KafkaException extends MessageException
{
	private static final long serialVersionUID = 1L;

	public KafkaException(String message)
	{
		super(message);
	}

	public KafkaException(Throwable e)
	{
		super(e);
	}

}
