package blue.internal.core.message;

import blue.core.message.MessageException;
import blue.core.util.AssertUtil;
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
	protected ConsumerListener listener;
	protected boolean multiThread;
	protected TaskExecutor taskExecutor;
	protected ExceptionHandler exceptionHandler;

	public ConsumerListenerConfig()
	{
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		AssertUtil.notEmpty(topic, "Topic");
		AssertUtil.notNull(listener, "ConsumerListener");

		if (!multiThread && taskExecutor != null)
			throw new MessageException(topic + " is not multi-thread，not need TaskExecutor consumer");

		ParameterizedType type = (ParameterizedType) listener.getClass().getGenericInterfaces()[0];
		clazz = (Class<?>) type.getActualTypeArguments()[0];
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

	public ConsumerListener getListener()
	{
		return listener;
	}

	public void setListener(ConsumerListener listener)
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

	public ExceptionHandler getExceptionHandler()
	{
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler)
	{
		this.exceptionHandler = exceptionHandler;
	}

	public Class<?> getClazz()
	{
		return clazz;
	}
}
