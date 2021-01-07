package blue.internal.jms.producer;

import blue.core.util.AssertUtil;
import blue.core.util.JsonUtil;
import blue.internal.core.message.ProducerListener;
import blue.internal.jms.consumer.DefaultMessageListener;
import blue.internal.jms.consumer.JmsListenerConfig;
import blue.jms.JmsTopic;
import blue.jms.JmsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

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
 * @since 1.0 2021-01-07
 */
public class JmsClient implements InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(JmsClient.class);

	private String name;
	private Connection connection;
	private Map<JmsTopic, Session> sessionMap = new ConcurrentHashMap<>();
	private Map<JmsTopic, MessageConsumer> consumerMap = new ConcurrentHashMap<>();

	public JmsClient()
	{
	}

	public void subscribe(JmsListenerConfig listenerConfig)
	{
		AssertUtil.notNull(listenerConfig, "JmsListenerConfig");
		JmsTopic topic = new JmsTopic(listenerConfig.getTopic(), listenerConfig.getType());
		if (sessionMap.containsKey(topic))
		{
			logger.warn("JmsTopic is exist: {}", topic);
			return;
		}
		try
		{
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = this.createDestination(session, topic);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new DefaultMessageListener(topic, listenerConfig));
			consumerMap.put(topic, consumer);
			sessionMap.put(topic, session);
			logger.info("JMS subscribe: {}", topic);
		}
		catch (JMSException e)
		{
			logger.error("Error: ", e);
		}
	}

	public void unsubscribe(JmsTopic topic)
	{
		AssertUtil.notNull(topic, "Topic");
		MessageConsumer consumer = consumerMap.remove(topic);
		this.close(consumer, "MessageConsumer");
		Session session = sessionMap.remove(topic);
		this.close(session, "Session");
	}

	public void send(JmsTopic topic, List<Object> messageList, List<ProducerListener<JmsTopic, Object>> listenerList)
	{
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		Session session = null;
		MessageProducer producer = null;
		Object currentObject = null;
		try
		{
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = this.createDestination(session, topic);
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			for (Object object : messageList)
			{
				currentObject = object;
				String json = JsonUtil.toString(object);
				TextMessage message = session.createTextMessage(json);
				producer.send(message);
				if (logger.isDebugEnabled())
				{
					logger.debug("Send message, {}: {}", topic.getType(), topic.getTopic());
				}
				for (ProducerListener<JmsTopic, Object> l : listenerList)
				{
					l.onSuccess(topic, object);
				}
			}
		}
		catch (Exception e)
		{
			for (ProducerListener<JmsTopic, Object> l : listenerList)
			{
				l.onFailure(topic, currentObject, e);
			}
			logger.error("Error: ", e);
		}
		finally
		{
			this.close(producer, "MessageProducer");
			this.close(session, "Session");
		}
	}

	private Destination createDestination(Session session, JmsTopic topic) throws JMSException
	{
		if (topic.getType() == null || topic.getType() == JmsType.QUEUE)
			return session.createQueue(topic.getTopic());

		return session.createTopic(topic.getTopic());
	}

	private void close(AutoCloseable closeable, String name)
	{
		if (closeable != null)
		{
			try
			{
				closeable.close();
			}
			catch (Exception e)
			{
				logger.error("JMS '{}' Close {} error, ", this.name, name, e);
			}
		}
	}

	@Override
	public void destroy() throws Exception
	{
		consumerMap.forEach((k, v) -> this.close(v, "MessageConsumer"));
		sessionMap.forEach((k, v) -> this.close(v, "Session"));
		this.close(connection, "Connection");
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		AssertUtil.notNull(connection, "Connection");
		connection.start();
		logger.info("JMS '{}' connected", name);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

}
