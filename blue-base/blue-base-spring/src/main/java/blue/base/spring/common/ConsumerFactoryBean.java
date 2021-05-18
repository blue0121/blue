package blue.base.spring.common;

import blue.base.core.message.ConsumerOptions;
import blue.base.core.message.ExceptionHandler;

import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public abstract class ConsumerFactoryBean {
    private String id;
    private boolean multiThread;
    private Executor executor;
    private ExceptionHandler exceptionHandler;

	public ConsumerFactoryBean() {
	}

	protected void setConsumerOptions(ConsumerOptions options) {
        options.setId(id)
                .setMultiThread(multiThread)
                .setExecutor(executor)
                .setExceptionHandler(exceptionHandler);
    }

    protected void setConsumerOptions(ConsumerOptions options, ConsumerConfig config) {
        if (config.getMultiThread() != null) {
            options.setMultiThread(config.getMultiThread());
        }
        if (config.getExecutor() != null) {
            options.setExecutor(config.getExecutor());
        }
        if (config.getExceptionHandler() != null) {
            options.setExceptionHandler(config.getExceptionHandler());
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMultiThread(boolean multiThread) {
        this.multiThread = multiThread;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
}
