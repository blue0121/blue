package blue.internal.jms.producer;

import blue.core.util.AssertUtil;
import blue.core.util.JsonUtil;
import blue.internal.core.message.ProducerListener;
import blue.internal.jms.consumer.DefaultMessageListener;
import blue.internal.jms.consumer.JmsListenerConfig;
import blue.jms.exception.JmsException;
import blue.jms.model.JmsTopic;
import blue.jms.model.JmsType;
import blue.jms.producer.JmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-08-02
 */
public class DefaultJmsProducer implements JmsProducer, InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultJmsProducer.class);

	private Connection connection;
	private TaskExecutor taskExecutor;
	private ProducerListener<JmsTopic, Object> listener;
	private Map<JmsTopic, Session> sessionMap = new HashMap<>();
	private Map<JmsTopic, MessageConsumer> consumerMap = new HashMap<>();

	public DefaultJmsProducer()
	{
	}

	public void addConsumerListener(JmsTopic topic, JmsListenerConfig listenerConfig)
	{
		AssertUtil.notNull(topic, "Topic");
		if (sessionMap.containsKey(topic))
		{
			logger.warn("JmsTopic is exist: {}", topic);
			return;
		}

		Destination destination = null;
		try
		{
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			if (topic.getType() == JmsType.QUEUE)
			{
				destination = session.createQueue(topic.getTopic());
			}
			else if (topic.getType() == JmsType.TOPIC)
			{
				destination = session.createTopic(topic.getTopic());
			}
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new DefaultMessageListener(topic, listenerConfig));
			sessionMap.put(topic, session);
			consumerMap.put(topic, consumer);
			logger.info("JMS subscribe: {}", topic);
		}
		catch (JMSException e)
		{
			logger.error("Error: ", e);
		}
	}

	@Override
	public void sendSync(JmsTopic topic, List<Object> messageList)
	{
		this.sendSync(topic, messageList, null);
	}

	private void sendSync(JmsTopic topic, List<Object> messageList, ProducerListener<JmsTopic, Object> listener)
	{
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		List<ProducerListener<JmsTopic, Object>> listenerList = this.getProducerListener(listener);
		Session session = null;
		MessageProducer producer = null;
		Destination destination = null;
		Object currentObject = null;
		try
		{
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			if (topic.getType() == JmsType.QUEUE)
			{
				destination = session.createQueue(topic.getTopic());
			}
			else if (topic.getType() == JmsType.TOPIC)
			{
				destination = session.createTopic(topic.getTopic());
			}
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			for (Object object : messageList)
			{
				currentObject = object;
				String json = null;
				if (object instanceof String)
				{
					json = (String)object;
				}
				else
				{
					json = JsonUtil.toString(object);
				}
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
			this.close(producer, session);
		}
	}

	private List<ProducerListener<JmsTopic, Object>> getProducerListener(ProducerListener<JmsTopic, Object> listener)
	{
		List<ProducerListener<JmsTopic, Object>> listenerList = new ArrayList<>();
		if (listener != null)
		{
			listenerList.add(listener);
		}
		if (this.listener != null)
		{
			listenerList.add(this.listener);
		}
		return listenerList;
	}

	private void close(MessageProducer producer, Session session)
	{
		if (producer != null)
		{
			try
			{
				producer.close();
			}
			catch (JMSException e)
			{
				logger.error("close producer error, ", e);
			}
		}
		if (session != null)
		{
			try
			{
				session.close();
			}
			catch (JMSException e)
			{
				logger.error("close session error, ", e);
			}
		}
	}

	@Override
	public void sendAsync(JmsTopic topic, List<Object> messageList, ProducerListener<JmsTopic, Object> listener)
	{
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		taskExecutor.execute(() -> sendSync(topic, messageList, listener));
	}

	@Override
	public void setProducerListener(ProducerListener<JmsTopic, Object> listener)
	{
		this.listener = listener;
	}

	@Override
	public void destroy() throws Exception
	{
		for (Map.Entry<JmsTopic, MessageConsumer> entry : consumerMap.entrySet())
		{
			try
			{
				entry.getValue().close();
			}
			catch (JMSException e)
			{
				logger.error("close consumer error, ", e);
			}
		}
		for (Map.Entry<JmsTopic, Session> entry : sessionMap.entrySet())
		{
			try
			{
				entry.getValue().close();
			}
			catch (JMSException e)
			{
				logger.error("close session error, ", e);
			}
		}
		if (connection != null)
		{
			connection.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (taskExecutor == null)
			throw new JmsException("taskExecutor is null");

		if (connection == null)
			throw new JmsException("connection is null");

		this.connection.start();
	}

	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}
}
