package blue.jms.internal.core.client;

import blue.base.core.message.ProducerListener;
import blue.base.core.util.AssertUtil;
import blue.base.core.util.JsonUtil;
import blue.jms.core.JmsClient;
import blue.jms.core.JmsConsumer;
import blue.jms.core.JmsProducer;
import blue.jms.core.JmsTopic;
import blue.jms.core.JmsType;
import blue.jms.core.options.JmsClientOptions;
import blue.jms.core.options.JmsConsumerOptions;
import blue.jms.core.options.JmsProducerOptions;
import blue.jms.internal.core.consumer.DefaultJmsConsumer;
import blue.jms.internal.core.consumer.DefaultMessageListener;
import blue.jms.internal.core.consumer.JmsListenerConfig;
import blue.jms.internal.core.producer.DefaultJmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public abstract class AbstractJmsClient implements JmsClient {
	private static Logger logger = LoggerFactory.getLogger(AbstractJmsClient.class);

	protected final JmsClientOptions options;
	protected Connection connection;
	protected final Map<JmsTopic, Session> sessionMap = new ConcurrentHashMap<>();
	protected final Map<JmsTopic, MessageConsumer> consumerMap = new ConcurrentHashMap<>();

	public AbstractJmsClient(JmsClientOptions options) {
		this.options = options;
		this.init();
	}

	protected abstract void init();

	public void subscribe(JmsListenerConfig config) {
		AssertUtil.notNull(config, "JmsListenerConfig");
		JmsTopic topic = new JmsTopic(config.getTopic(), config.getType());
		if (sessionMap.containsKey(topic)) {
			logger.warn("JmsTopic is exist: {}", topic);
			return;
		}
		try {
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = this.createDestination(session, topic);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new DefaultMessageListener(topic, config));
			consumerMap.put(topic, consumer);
			sessionMap.put(topic, session);
			logger.info("JMS subscribe: {}", topic);
		}
		catch (JMSException e) {
			logger.error("Error: ", e);
		}
	}

	public void unsubscribe(JmsTopic topic) {
		AssertUtil.notNull(topic, "Topic");
		MessageConsumer consumer = consumerMap.remove(topic);
		this.close(consumer, "MessageConsumer");
		Session session = sessionMap.remove(topic);
		this.close(session, "Session");
	}

	public void send(JmsTopic topic, List<Object> messageList, List<ProducerListener<JmsTopic, Object>> listenerList) {
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		Session session = null;
		MessageProducer producer = null;
		Object currentObject = null;
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = this.createDestination(session, topic);
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			for (Object object : messageList) {
				currentObject = object;
				String json = JsonUtil.toString(object);
				TextMessage message = session.createTextMessage(json);
				producer.send(message);
				if (logger.isDebugEnabled()) {
					logger.debug("Send message, {}: {}", topic.getType(), topic.getTopic());
				}
				for (ProducerListener<JmsTopic, Object> l : listenerList) {
					l.onSuccess(topic, object);
				}
			}
		}
		catch (Exception e) {
			for (ProducerListener<JmsTopic, Object> l : listenerList) {
				l.onFailure(topic, currentObject, e);
			}
			logger.error("Error: ", e);
		}
		finally {
			this.close(producer, "MessageProducer");
			this.close(session, "Session");
		}
	}

	protected Destination createDestination(Session session, JmsTopic topic) throws JMSException {
		if (topic.getType() == null || topic.getType() == JmsType.QUEUE) {
			return session.createQueue(topic.getTopic());
		}

		return session.createTopic(topic.getTopic());
	}

	protected void close(AutoCloseable closeable, String name) {
		if (closeable != null) {
			try {
				closeable.close();
			}
			catch (Exception e) {
				logger.error("JMS '{}' Close {} error, ", options.getId(), name, e);
			}
		}
	}

	@Override
	public void disconnect() {
		consumerMap.forEach((k, v) -> this.close(v, "MessageConsumer"));
		sessionMap.forEach((k, v) -> this.close(v, "Session"));
		this.close(connection, "Connection");
	}

	@Override
	public JmsProducer createProducer(JmsProducerOptions options) {
		return new DefaultJmsProducer(options, this);
	}

	@Override
	public JmsConsumer createConsumer(JmsConsumerOptions options) {
		return new DefaultJmsConsumer(options, this);
	}
}
