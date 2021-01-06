package blue.internal.core.message;

import blue.core.message.Topic;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-06
 */
public abstract class AbstractProducer<T extends Topic> implements Producer<T>, InitializingBean
{
	protected String name;

	public AbstractProducer()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
