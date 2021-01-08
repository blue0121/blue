package blue.internal.kafka.consumer;

import blue.core.util.AssertUtil;
import blue.internal.core.message.AbstractConsumer;
import blue.internal.core.message.ConsumerListener;
import blue.internal.core.message.ConsumerListenerConfig;
import blue.internal.kafka.codec.FastjsonDeserializer;
import blue.internal.kafka.offset.MemoryOffsetManager;
import blue.internal.kafka.offset.OffsetManager;
import blue.kafka.KafkaTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public class DefaultKafkaConsumer extends AbstractConsumer<KafkaTopic> implements InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultKafkaConsumer.class);

	private Properties config;
	private OffsetManager offsetManager;
	private Map<String, List<ConsumerRunnable>> consumerMap = new ConcurrentHashMap<>();

	public DefaultKafkaConsumer()
	{
	}

	@Override
	protected void subscribe(List<ConsumerListenerConfig> configList)
	{
		for (ConsumerListenerConfig config : configList)
		{
			KafkaListenerConfig listenerConfig = (KafkaListenerConfig) config;
			if (listenerConfig.getOffsetManager() == null)
			{
				listenerConfig.setOffsetManager(offsetManager);
			}
			for (int i = 0; i < listenerConfig.getCount(); i++)
			{
				ConsumerRunnable consumer = new ConsumerThread(this.config, listenerConfig);
				List<ConsumerRunnable> consumerList = consumerMap.computeIfAbsent(config.getTopic(), k -> new ArrayList<>());
				consumerList.add(consumer);
				Thread thread = new Thread(consumer);
				thread.start();
			}
		}
	}

	@Override
	public void subscribe(Collection<KafkaTopic> topicList, ConsumerListener<KafkaTopic, ?> listener)
	{
		AssertUtil.notEmpty(topicList, "Topic list");
		List<ConsumerListenerConfig> configList = new ArrayList<>();
		for (var topic : topicList)
		{
			var config = new KafkaListenerConfig();
			config.setTopic(topic.getTopic());
			config.setMultiThread(taskExecutor != null);
			config.setListener(listener);
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
			List<ConsumerRunnable> consumerList = consumerMap.remove(topic);
			if (consumerList != null && !consumerList.isEmpty())
			{
				consumerList.forEach(e -> e.destroy());
			}
		}
	}

	@Override
	public void destroy() throws Exception
	{
		for (var entry : consumerMap.entrySet())
		{
			if (entry.getValue() != null && !entry.getValue().isEmpty())
			{
				entry.getValue().forEach(e -> e.destroy());
			}
		}
		logger.info("Kafka '{}' destroy", name);
	}

	@Override
	protected void check()
	{
		super.check();
		AssertUtil.notNull(config, "Config");

		if (!this.config.containsKey(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG))
		{
			logger.info("Kafka '{}' config '{}' is empty, default: {}", ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, name, StringDeserializer.class.getName());
			this.config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		}
		if (!this.config.containsKey(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG))
		{
			logger.info("Kafka '{}' config '{}' is empty, default: {}", ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, name, FastjsonDeserializer.class.getName());
			this.config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, FastjsonDeserializer.class.getName());
		}

		if (offsetManager == null)
		{
			offsetManager = new MemoryOffsetManager();
			logger.info("Kafka '{}' OffsetManager is null，default: MemoryOffsetManager", name);
		}
	}

	public void setConfig(Properties config)
	{
		this.config = config;
	}

	public void setOffsetManager(OffsetManager offsetManager)
	{
		this.offsetManager = offsetManager;
	}
}
