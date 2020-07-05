package blue.mqtt.exception;

import blue.core.message.MessageException;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class MqttException extends MessageException
{
	private static final long serialVersionUID = 1L;

	public MqttException(String message)
	{
		super(message);
	}

	public MqttException(Throwable e)
	{
		super(e);
	}
}
