package blue.base.core.message;

import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2021-01-01
 */
public interface Consumer<T extends Topic> {

	/**
	 * 订阅
	 *
	 * @param topic
	 * @param listener
	 */
	default void subscribe(T topic, ConsumerListener<T, ?> listener) {
		this.subscribe(List.of(topic), listener);
	}

	/**
	 * 订阅
	 *
	 * @param topicList
	 * @param listener
	 */
	void subscribe(Collection<T> topicList, ConsumerListener<T, ?> listener);

	/**
	 * 取消订阅
	 *
	 * @param topic
	 */
	default void unsubscribe(String topic) {
		this.unsubscribe(List.of(topic));
	}

	/**
	 * 取消订阅
	 *
	 * @param topicList
	 */
	void unsubscribe(Collection<String> topicList);

}
