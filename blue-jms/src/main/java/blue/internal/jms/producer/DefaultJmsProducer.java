package blue.internal.jms.producer;

import blue.core.util.AssertUtil;
import blue.internal.core.message.AbstractProducer;
import blue.internal.core.message.ProducerListener;
import blue.jms.JmsProducer;
import blue.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2019-08-02
 */
public class DefaultJmsProducer extends AbstractProducer<JmsTopic> implements JmsProducer
{
	private static Logger logger = LoggerFactory.getLogger(DefaultJmsProducer.class);

	private JmsClient jmsClient;
	private TaskExecutor taskExecutor;

	public DefaultJmsProducer()
	{
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
		jmsClient.send(topic, messageList, listenerList);
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

	@Override
	public void sendAsync(JmsTopic topic, List<Object> messageList, ProducerListener<JmsTopic, Object> listener)
	{
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		taskExecutor.execute(() -> sendSync(topic, messageList, listener));
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		AssertUtil.notNull(jmsClient, "JmsClient");
		super.afterPropertiesSet();
	}

	public void setJmsClient(JmsClient jmsClient)
	{
		this.jmsClient = jmsClient;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}
}
