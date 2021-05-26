package blue.jms.internal.core.consumer;

import blue.base.core.message.ConsumerListener;
import blue.base.core.util.AssertUtil;
import blue.base.internal.core.message.AbstractConsumer;
import blue.jms.core.JmsConsumer;
import blue.jms.core.JmsTopic;
import blue.jms.core.options.JmsConsumerOptions;
import blue.jms.internal.core.client.AbstractJmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public class DefaultJmsConsumer extends AbstractConsumer<JmsTopic> implements JmsConsumer {
	private static Logger logger = LoggerFactory.getLogger(DefaultJmsConsumer.class);

	private final JmsConsumerOptions options;
	private final AbstractJmsClient jmsClient;

	public DefaultJmsConsumer(JmsConsumerOptions options, AbstractJmsClient jmsClient) {
		super(options);
		this.options = options;
		this.jmsClient = jmsClient;
	}

	@Override
	public void subscribe(Collection<JmsTopic> topicList, ConsumerListener<JmsTopic, ?> listener) {
		AssertUtil.notEmpty(topicList, "Topic List");
		AssertUtil.notNull(listener, "ConsumerListener");
		for (JmsTopic topic : topicList) {
			JmsListenerConfig config = new JmsListenerConfig();
			config.setTopic(topic.getTopic());
			config.setType(topic.getType());
			config.setMultiThread(options.isMultiThread());
			config.setListener(listener);
			config.setExceptionHandler(options.getExceptionHandler());
			config.setExecutor(options.getExecutor());
			config.init();
			jmsClient.subscribe(config);
		}
	}

	@Override
	public void unsubscribe(Collection<String> topicList) {
		AssertUtil.notEmpty(topicList, "Topic List");
		for (String topic : topicList) {
			jmsClient.unsubscribe(new JmsTopic(topic, null));
		}
	}

}
