package blue.internal.kafka.consumer;

/**
 * Kafka Consumer 执行线程接口
 *
 * @author Jin Zheng
 * @since 1.0 2019-02-28
 */
public interface ConsumerRunnable extends Runnable
{
	/**
	 * 线程销毁
	 */
	void destroy();
}
