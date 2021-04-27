package blue.base.internal.core.message;

import blue.base.core.message.Consumer;
import blue.base.core.message.ExceptionHandler;
import blue.base.core.message.MessageException;
import blue.base.core.message.Topic;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public abstract class AbstractConsumer<T extends Topic> implements Consumer<T> {
	private static Logger logger = LoggerFactory.getLogger(AbstractConsumer.class);

	protected String name;
	protected List<ConsumerListenerConfig> configList;
	protected Executor executor;
	protected ExceptionHandler<Topic, Object> exceptionHandler;

	public AbstractConsumer() {
	}

	public void init() {
		this.check();
		logger.info("Consumer '{}' ConsumerListener size: {}", name, configList.size());
		this.subscribe(configList);
	}

	protected abstract void subscribe(List<ConsumerListenerConfig> configList);

	protected void check() {
		if (exceptionHandler == null) {
			logger.info("Consumer '{}' default ExceptionHandler is null, use LoggerExceptionHandler", name);
			exceptionHandler = new LoggerExceptionHandler<>();
		}
		this.checkHandler(configList);
		if (this.isMultiThread() && executor == null) {
			throw new MessageException("TaskExecutor config is null");
		}
	}

	protected void checkHandler(List<ConsumerListenerConfig> configList) {
		AssertUtil.notEmpty(configList, "ConsumerListenerList");
		for (var config : configList) {
			if (config.getExceptionHandler() == null) {
				config.setExceptionHandler(exceptionHandler);
			}
			if (config.getExecutor() == null) {
				config.setExecutor(executor);
			}
		}
	}

	private boolean isMultiThread() {
		for (ConsumerListenerConfig config : configList) {
			if (config.isMultiThread()) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ConsumerListenerConfig> getConfigList() {
		return configList;
	}

	public void setConfigList(List<ConsumerListenerConfig> configList) {
		this.configList = configList;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setTaskExecutor(Executor executor) {
		this.executor = executor;
	}

	public ExceptionHandler<Topic, Object> getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler<Topic, Object> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
