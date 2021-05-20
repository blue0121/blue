package blue.base.internal.core.message;

import blue.base.core.message.ConsumerListener;
import blue.base.core.message.ExceptionHandler;
import blue.base.core.message.MessageException;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public class ConsumerListenerConfig {
	private static Logger logger = LoggerFactory.getLogger(ConsumerListenerConfig.class);

	protected Class<?> clazz;
	protected String topic;
	protected ConsumerListener listener;
	protected boolean multiThread;
	protected Executor executor;
	protected ExceptionHandler exceptionHandler;

	public ConsumerListenerConfig() {
	}

	public void init() {
		AssertUtil.notEmpty(topic, "Topic");
		AssertUtil.notNull(listener, "ConsumerListener");

		if (!multiThread && executor == null) {
			throw new MessageException(topic + " is multi-thread，Executor is not null");
		}

		ParameterizedType type = (ParameterizedType) listener.getClass().getGenericInterfaces()[0];
		clazz = (Class<?>) type.getActualTypeArguments()[0];
	}

	@Override
	public String toString() {
		return String.format("ConsumerListenerConfig[topic=%s, multi-thread=%s]", topic, multiThread);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public ConsumerListener getListener() {
		return listener;
	}

	public void setListener(ConsumerListener listener) {
		this.listener = listener;
	}

	public boolean isMultiThread() {
		return multiThread;
	}

	public void setMultiThread(boolean multiThread) {
		this.multiThread = multiThread;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public Class<?> getClazz() {
		return clazz;
	}
}
