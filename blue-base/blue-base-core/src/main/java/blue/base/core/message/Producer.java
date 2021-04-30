package blue.base.core.message;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public interface Producer<T extends Topic> {
	/**
	 * 同步发送消息
	 *
	 * @param topic
	 * @param message
	 */
	default void sendSync(T topic, Object message) {
		this.sendSync(topic, List.of(message));
	}

	/**
	 * 同步发送消息
	 *
	 * @param topic
	 * @param messageList
	 */
	void sendSync(T topic, List<Object> messageList);

	/**
	 * 异步发送消息
	 *
	 * @param topic
	 * @param message
	 */
	default void sendAsync(T topic, Object message) {
		this.sendAsync(topic, List.of(message), null);
	}

	/**
	 * 异步发送消息
	 *
	 * @param topic
	 * @param messageList
	 */
	default void sendAsync(T topic, List<Object> messageList) {
		this.sendAsync(topic, messageList, null);
	}

	/**
	 * 异步发送消息
	 *
	 * @param topic
	 * @param message
	 * @param listener
	 */
	default void sendAsync(T topic, Object message, ProducerListener<T, Object> listener) {
		this.sendAsync(topic, List.of(message), listener);
	}

	/**
	 * 异步发送消息
	 *
	 * @param topic
	 * @param messageList
	 * @param listener
	 */
	void sendAsync(T topic, List<Object> messageList, ProducerListener<T, Object> listener);

}
