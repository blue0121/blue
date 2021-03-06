package blue.jms.core;

import blue.base.core.message.ExceptionHandler;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public interface JmsExceptionHandler<T> extends ExceptionHandler<JmsTopic, T> {
}
