package blue.internal.jms.consumer;

import blue.internal.core.message.AbstractListenerContainer;
import blue.internal.core.message.ConsumerListenerConfig;
import blue.internal.jms.producer.DefaultJmsProducer;
import blue.jms.exception.JmsException;
import blue.jms.model.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-08-02
 */
public class JmsListenerContainer extends AbstractListenerContainer
{
	private static Logger logger = LoggerFactory.getLogger(JmsListenerContainer.class);

	private DefaultJmsProducer producer;

	public JmsListenerContainer()
	{
	}

	@Override
	protected void start()
	{
		for (ConsumerListenerConfig config : configList)
		{
			JmsListenerConfig jmsConfig = (JmsListenerConfig) config;
			JmsTopic topic = new JmsTopic(jmsConfig.getTopic(), jmsConfig.getType());
			producer.addConsumerListener(topic, jmsConfig);
		}
	}

	@Override
	protected void check()
	{
		super.check();
		if (producer == null)
			throw new JmsException("jmsProducer config is null");
	}

	public DefaultJmsProducer getProducer()
	{
		return producer;
	}

	public void setProducer(DefaultJmsProducer producer)
	{
		this.producer = producer;
	}
}
