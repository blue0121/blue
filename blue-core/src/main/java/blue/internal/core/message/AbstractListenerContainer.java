package blue.internal.core.message;

import blue.core.message.ExceptionHandler;
import blue.core.message.MessageException;
import blue.core.message.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public abstract class AbstractListenerContainer implements InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(AbstractListenerContainer.class);

	protected List<ConsumerListenerConfig> configList;
	protected TaskExecutor taskExecutor;
	protected ExceptionHandler<Topic, Object> exceptionHandler;

	public AbstractListenerContainer()
	{
	}

	@Override
	public void destroy() throws Exception
	{
		logger.info("Destroy AbstractListenerContainer");
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		this.check();
		logger.info("ConsumerListener size: {}", configList.size());
		this.start();
	}

	protected abstract void start();

	protected void check()
	{
		if (configList == null || configList.isEmpty())
			throw new MessageException("ConsumerListener config is null");

		if (exceptionHandler == null)
		{
			logger.info("Default ExceptionHandler is empty, use LoggerExceptionHandler");
			exceptionHandler = new LoggerExceptionHandler<>();
		}

		if (this.isMultiThread() && taskExecutor == null)
			throw new MessageException("TaskExecutor config is null");

		for (ConsumerListenerConfig config : configList)
		{
			if (config.getExceptionHandler() == null)
			{
				config.setExceptionHandler(exceptionHandler);
			}
			if (config.getTaskExecutor() == null)
			{
				config.setTaskExecutor(taskExecutor);
			}
		}
	}

	private boolean isMultiThread()
	{
		for (ConsumerListenerConfig config : configList)
		{
			if (config.isMultiThread())
				return true;
		}
		return false;
	}

	public List<ConsumerListenerConfig> getConfigList()
	{
		return configList;
	}

	public void setConfigList(List<ConsumerListenerConfig> configList)
	{
		this.configList = configList;
	}

	public TaskExecutor getTaskExecutor()
	{
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	public ExceptionHandler<Topic, Object> getExceptionHandler()
	{
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler<Topic, Object> exceptionHandler)
	{
		this.exceptionHandler = exceptionHandler;
	}
}
