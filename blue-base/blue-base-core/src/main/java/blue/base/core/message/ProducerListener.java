package blue.base.core.message;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public interface ProducerListener<T extends Topic, V> {

	/**
	 * 成功时
	 *
	 * @param topic
	 * @param message
	 */
	void onSuccess(T topic, V message);

	/**
	 * 失败时
	 *
	 * @param topic
	 * @param message
	 * @param cause
	 */
	void onFailure(T topic, V message, Throwable cause);

}
