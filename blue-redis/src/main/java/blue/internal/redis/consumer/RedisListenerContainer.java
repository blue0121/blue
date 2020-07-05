package blue.internal.redis.consumer;

import blue.internal.core.message.AbstractListenerContainer;
import blue.internal.core.message.ConsumerListenerConfig;
import org.redisson.api.RedissonClient;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class RedisListenerContainer extends AbstractListenerContainer
{
	private RedissonClient redisson;

	public RedisListenerContainer()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void start()
	{
		for (ConsumerListenerConfig config : configList)
		{
			RedissonMessageListener listener = new RedissonMessageListener(config);
			redisson.getTopic(config.getTopic()).addListener(config.getClazz(), listener);
		}
	}

	public void setRedisson(RedissonClient redisson)
	{
		this.redisson = redisson;
	}
}
