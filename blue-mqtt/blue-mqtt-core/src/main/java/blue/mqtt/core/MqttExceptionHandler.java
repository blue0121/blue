package blue.mqtt.core;

import blue.base.core.message.ExceptionHandler;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-06
 */
public interface MqttExceptionHandler<T> extends ExceptionHandler<MqttTopic, T> {

}
