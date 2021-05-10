package blue.mqtt.internal.spring.bean;

import blue.base.core.message.ConsumerListener;
import blue.base.core.message.ExceptionHandler;

import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttConsumerConfig {
    private String topic;
    private Integer qos;
    private Boolean multiThread;
    private Executor executor;
    private ExceptionHandler exceptionHandler;
    private ConsumerListener listener;

	public MqttConsumerConfig() {
	}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
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
