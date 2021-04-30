package blue.base.core.message;

import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-29
 */
@SuppressWarnings("rawtypes")
public class ConsumerOptions {
    private String id;
    private boolean multiThread;
    private Executor executor;
    private ExceptionHandler exceptionHandler;

	public ConsumerOptions() {
	}

    public String getId() {
        return id;
    }

    public ConsumerOptions setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isMultiThread() {
        return multiThread;
    }

    public ConsumerOptions setMultiThread(boolean multiThread) {
        this.multiThread = multiThread;
        return this;
    }

    public Executor getExecutor() {
        return executor;
    }

    public ConsumerOptions setExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }

    public <T extends Topic> ExceptionHandler<T, ?> getExceptionHandler() {
        return exceptionHandler;
    }

    public ConsumerOptions setExceptionHandler(ExceptionHandler<? extends Topic, ?> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }
}
