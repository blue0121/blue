package blue.base.internal.core.message;

import blue.base.core.message.Consumer;
import blue.base.core.message.ConsumerOptions;
import blue.base.core.message.MessageException;
import blue.base.core.message.Topic;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public abstract class AbstractConsumer<T extends Topic> implements Consumer<T> {
	private static Logger logger = LoggerFactory.getLogger(AbstractConsumer.class);

	protected final ConsumerOptions options;
	protected List<ConsumerListenerConfig> configList;

	public AbstractConsumer(ConsumerOptions options) {
		AssertUtil.notNull(options, "Consumer Options");
		options.check();
		this.options = options;
	}

	public void init() {
		this.check();
		if (configList != null && !configList.isEmpty()) {
			logger.info("Consumer '{}' ConsumerListener size: {}", options.getId(),
					configList == null ? 0 : configList.size());
		}
		this.subscribe(configList);
	}

	protected abstract void subscribe(List<ConsumerListenerConfig> configList);

	protected void check() {
		if (options.getExceptionHandler() == null) {
			logger.info("Consumer '{}' default ExceptionHandler is null, use LoggerExceptionHandler", options.getId());
			options.setExceptionHandler(new LoggerExceptionHandler<>());
		}
		this.checkHandler(configList);
		if (this.isMultiThread() && options.getExecutor() == null) {
			throw new MessageException("Executor config is null");
		}
	}

	protected void checkHandler(List<ConsumerListenerConfig> configList) {
		if (configList == null || configList.isEmpty()) {
			return;
		}
		for (var config : configList) {
			if (config.getExceptionHandler() == null) {
				config.setExceptionHandler(options.getExceptionHandler());
			}
			if (config.getExecutor() == null) {
				config.setExecutor(options.getExecutor());
			}
		}
	}

	private boolean isMultiThread() {
		if (configList == null || configList.isEmpty()) {
			return false;
		}
		for (ConsumerListenerConfig config : configList) {
			if (config.isMultiThread()) {
				return true;
			}
		}
		return false;
	}

	public List<ConsumerListenerConfig> getConfigList() {
		return configList;
	}

	public void setConfigList(List<ConsumerListenerConfig> configList) {
		this.configList = configList;
	}
}
