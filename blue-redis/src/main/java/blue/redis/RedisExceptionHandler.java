package blue.redis;

import blue.core.message.Topic;
import blue.internal.core.message.ExceptionHandler;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-08
 */
public interface RedisExceptionHandler<T> extends ExceptionHandler<Topic, T>
{
}
