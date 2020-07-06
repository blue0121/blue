package blue.internal.jms.consumer;

import blue.internal.core.message.ConsumerListenerConfig;
import blue.jms.exception.JmsException;
import blue.jms.model.JmsType;

/**
 * @author Jin Zheng
 * @since 2019-08-02
 */
public class JmsListenerConfig extends ConsumerListenerConfig
{
	protected JmsType type;

	public JmsListenerConfig()
	{
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		super.afterPropertiesSet();
		if (type == null)
			throw new JmsException("type is empty");
	}

	@Override
	public String toString()
	{
		return String.format("JmsListenerConfig[topic=%s, type: %s, multi-thread=%s]",
				topic, type.name(), multiThread);
	}

	public JmsType getType()
	{
		return type;
	}

	public void setType(JmsType type)
	{
		this.type = type;
	}
}
