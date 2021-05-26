package blue.jms.internal.core.producer;


import blue.base.core.message.ProducerListener;
import blue.base.core.util.AssertUtil;
import blue.base.internal.core.message.AbstractProducer;
import blue.jms.core.JmsProducer;
import blue.jms.core.JmsTopic;
import blue.jms.core.options.JmsProducerOptions;
import blue.jms.internal.core.client.AbstractJmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2019-08-02
 */
public class DefaultJmsProducer extends AbstractProducer<JmsTopic> implements JmsProducer {
	private static Logger logger = LoggerFactory.getLogger(DefaultJmsProducer.class);

	private final JmsProducerOptions options;
	private final AbstractJmsClient jmsClient;

	public DefaultJmsProducer(JmsProducerOptions options, AbstractJmsClient jmsClient) {
		super(options);
		this.options = options;
		this.jmsClient = jmsClient;
	}

	@Override
	public void sendSync(JmsTopic topic, List<Object> messageList) {
		this.sendSync(topic, messageList, null);
	}

	private void sendSync(JmsTopic topic, List<Object> messageList, ProducerListener<JmsTopic, Object> listener) {
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		List<ProducerListener<JmsTopic, Object>> listenerList = this.getProducerListener(listener);
		jmsClient.send(topic, messageList, listenerList);
	}

	private List<ProducerListener<JmsTopic, Object>> getProducerListener(ProducerListener<JmsTopic, Object> listener) {
		List<ProducerListener<JmsTopic, Object>> listenerList = new ArrayList<>();
		if (listener != null) {
			listenerList.add(listener);
		}
		if (options.getProducerListener() != null) {
			listenerList.add(options.getProducerListener());
		}
		return listenerList;
	}

	@Override
	public void sendAsync(JmsTopic topic, List<Object> messageList, ProducerListener<JmsTopic, Object> listener) {
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		options.getExecutor().execute(() -> sendSync(topic, messageList, listener));
	}

}
