package blue.base.spring.common;

import blue.base.core.message.ConsumerListener;
import blue.base.core.message.ExceptionHandler;

import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public class ConsumerConfig {
    private String topic;
    private Boolean multiThread;
    private Executor executor;
    private ExceptionHandler exceptionHandler;
    private ConsumerListener listener;

	public ConsumerConfig() {
	}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Boolean getMultiThread() {
        return multiThread;
    }

    public void setMultiThread(Boolean multiThread) {
        this.multiThread = multiThread;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public ConsumerListener getListener() {
        return listener;
    }

    public void setListener(ConsumerListener listener) {
        this.listener = listener;
    }
}
