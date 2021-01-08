package blue.internal.redis.consumer;

import blue.core.message.Topic;
import blue.core.util.AssertUtil;
import blue.internal.core.message.AbstractConsumer;
import blue.internal.core.message.ConsumerListener;
import blue.internal.core.message.ConsumerListenerConfig;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-08
 */
public class DefaultRedisConsumer extends AbstractConsumer<Topic>
{
	private RedissonClient redisson;

	public DefaultRedisConsumer()
	{
	}

	@Override
	protected void subscribe(List<ConsumerListenerConfig> configList)
	{
		for (ConsumerListenerConfig config : configList)
		{
			RedissonMessageListener listener = new RedissonMessageListener(config);
			redisson.getTopic(config.getTopic()).addListener(config.getClazz(), listener);
		}
	}

	@Override
	public void subscribe(Collection<Topic> topicList, ConsumerListener<Topic, ?> listener)
	{
		AssertUtil.notEmpty(topicList, "Topic list");
		AssertUtil.notNull(listener, "ConsumerListener");
		List<ConsumerListenerConfig> configList = new ArrayList<>();
		for (Topic topic : topicList)
		{
			ConsumerListenerConfig config = new ConsumerListenerConfig();
			config.setTopic(topic.getTopic());
			config.setListener(listener);
			config.setMultiThread(taskExecutor != null);
			config.afterPropertiesSet();
			configList.add(config);
		}
		this.checkHandler(configList);
		this.subscribe(configList);
	}

	@Override
	public void unsubscribe(Collection<String> topicList)
	{
		AssertUtil.notEmpty(topicList, "Topic list");
		for (String topic : topicList)
		{
			redisson.getTopic(topic).removeAllListeners();
		}
	}

	public void setRedisson(RedissonClient redisson)
	{
		this.redisson = redisson;
	}
}
