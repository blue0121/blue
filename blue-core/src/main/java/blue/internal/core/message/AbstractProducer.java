package blue.internal.core.message;

import blue.core.message.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-06
 */
public abstract class AbstractProducer<T extends Topic> implements Producer<T>, InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(AbstractProducer.class);

	protected String name;
	protected ProducerListener<T, Object> listener;

	public AbstractProducer()
	{
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (this.listener == null)
		{
			this.listener = new LoggerProducerListener<>();
			logger.info("Producer '{}' default ProducerListener is null, use LoggerProducerListener", name);
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void setProducerListener(ProducerListener<T, Object> listener)
	{
		this.listener = listener;
	}
}
