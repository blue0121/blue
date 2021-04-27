package blue.base.core.message;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-28
 */
public interface ConsumerListener<T extends Topic, V> {
	/**
	 * 收到消息
	 *
	 * @param topic
	 * @param message
	 */
	void onReceive(T topic, V message);

}
