package blue.internal.core.message;

import blue.core.message.ConsumerListener;
import blue.core.message.ExceptionHandler;
import blue.core.message.MessageException;
import blue.core.message.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;

import java.lang.reflect.ParameterizedType;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public class ConsumerListenerConfig implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(ConsumerListenerConfig.class);

	protected Class<?> clazz;
	protected String topic;
	protected ConsumerListener<Topic, Object> listener;
	protected boolean multiThread;
	protected TaskExecutor taskExecutor;
	protected ExceptionHandler<Topic, Object> exceptionHandler;

	public ConsumerListenerConfig()
	{
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (topic == null || topic.isEmpty())
			throw new MessageException("Topic is empty");

		if (listener == null)
			throw new MessageException(topic + " ConsumerListener is empty");

		if (!multiThread && taskExecutor != null)
			throw new MessageException(topic + " is not multi-thread，not need TaskExecutor consumer");

		ParameterizedType type = (ParameterizedType) listener.getClass().getGenericInterfaces()[0];
		clazz = (Class<?>) type.getActualTypeArguments()[1];
	}

	@Override
	public String toString()
	{
		return String.format("ConsumerListenerConfig[topic=%s, multi-thread=%s]", topic, multiThread);
	}

	public String getTopic()
	{
		return topic;
	}

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public TaskExecutor getTaskExecutor()
	{
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	public ConsumerListener<Topic, Object> getListener()
	{
		return listener;
	}

	public void setListener(ConsumerListener<Topic, Object> listener)
	{
		this.listener = listener;
	}

	public boolean isMultiThread()
	{
		return multiThread;
	}

	public void setMultiThread(boolean multiThread)
	{
		this.multiThread = multiThread;
	}

	public ExceptionHandler<Topic, Object> getExceptionHandler()
	{
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler<Topic, Object> exceptionHandler)
	{
		this.exceptionHandler = exceptionHandler;
	}

	public Class<?> getClazz()
	{
		return clazz;
	}
}
