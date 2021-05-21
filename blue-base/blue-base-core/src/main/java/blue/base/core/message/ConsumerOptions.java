package blue.base.core.message;

import blue.base.core.id.IdGenerator;
import blue.base.internal.core.message.LoggerExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-29
 */
@SuppressWarnings("rawtypes")
public class ConsumerOptions {
    private static Logger logger = LoggerFactory.getLogger(ConsumerOptions.class);

    protected String id;
    protected boolean multiThread;
    protected Executor executor;
    protected ExceptionHandler exceptionHandler;

    public ConsumerOptions() {
    }

    public void check() {
        if (multiThread && executor == null) {
            throw new MessageException("Executor config is null");
        }
        if (id == null || id.isEmpty()) {
            id = "Consumer" + IdGenerator.uuid12bit();
        }
        if (exceptionHandler == null) {
            logger.info("Consumer '{}' default ExceptionHandler is null, use LoggerExceptionHandler", id);
            exceptionHandler = new LoggerExceptionHandler<>();
        }
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
