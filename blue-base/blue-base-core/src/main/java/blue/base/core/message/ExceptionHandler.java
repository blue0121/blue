package blue.base.core.message;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-28
 */
public interface ExceptionHandler<T extends Topic, V> {

	/**
	 * 异常处理
	 *
	 * @param topic
	 * @param message
	 * @param e
	 */
	void onError(T topic, V message, Exception e);

}
