package blue.internal.core.message;

import blue.core.message.MessageException;
import blue.core.message.Topic;
import blue.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public abstract class AbstractConsumer<T extends Topic> implements Consumer<T>, InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(AbstractConsumer.class);

	protected String name;
	protected List<ConsumerListenerConfig> configList;
	protected TaskExecutor taskExecutor;
	protected ExceptionHandler<Topic, Object> exceptionHandler;

	public AbstractConsumer()
	{
	}

	@Override
	public void afterPropertiesSet()
	{
		this.check();
		logger.info("Consumer '{}' ConsumerListener size: {}", name, configList.size());
		this.subscribe(configList);
	}

	protected abstract void subscribe(List<ConsumerListenerConfig> configList);

	protected void check()
	{
		if (exceptionHandler == null)
		{
			logger.info("Consumer '{}' default ExceptionHandler is empty, use LoggerExceptionHandler", name);
			exceptionHandler = new LoggerExceptionHandler<>();
		}

		if (this.isMultiThread() && taskExecutor == null)
			throw new MessageException("TaskExecutor config is null");

		this.checkHandler(configList);
	}

	protected void checkHandler(List<ConsumerListenerConfig> configList)
	{
		AssertUtil.notEmpty(configList, "ConsumerListenerList");
		for (var config : configList)
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
