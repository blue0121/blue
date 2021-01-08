package blue.internal.jms.consumer;

import blue.core.util.AssertUtil;
import blue.internal.core.message.AbstractConsumer;
import blue.internal.core.message.ConsumerListener;
import blue.internal.core.message.ConsumerListenerConfig;
import blue.internal.jms.producer.JmsClient;
import blue.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public class DefaultJmsConsumer extends AbstractConsumer<JmsTopic> implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultJmsConsumer.class);

	private JmsClient jmsClient;

	public DefaultJmsConsumer()
	{
	}

	@Override
	protected void subscribe(List<ConsumerListenerConfig> configList)
	{
		for (ConsumerListenerConfig config : configList)
		{
			jmsClient.subscribe((JmsListenerConfig) config);
		}
	}

	@Override
	public void subscribe(Collection<JmsTopic> topicList, ConsumerListener<JmsTopic, ?> listener)
	{
		AssertUtil.notEmpty(topicList, "Topic List");
		AssertUtil.notNull(listener, "ConsumerListener");
		List<ConsumerListenerConfig> configList = new ArrayList<>();
		for (JmsTopic topic : topicList)
		{
			JmsListenerConfig config = new JmsListenerConfig();
			config.setTopic(topic.getTopic());
			config.setType(topic.getType());
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
		AssertUtil.notEmpty(topicList, "Topic List");
		for (String topic : topicList)
		{
			jmsClient.unsubscribe(new JmsTopic(topic, null));
		}
	}

	@Override
	public void afterPropertiesSet()
	{
		super.afterPropertiesSet();
		AssertUtil.notNull(jmsClient, "JmsClient");
	}

	public void setJmsClient(JmsClient jmsClient)
	{
		this.jmsClient = jmsClient;
	}
}
