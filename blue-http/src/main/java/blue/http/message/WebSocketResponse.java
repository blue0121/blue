package blue.http.message;

/**
 * @author Jin Zheng
 * @since 2020-02-05
 */
public interface WebSocketResponse
{
	/**
	 * 获取错误代码
	 *
	 * @return
	 */
	int getCode();

	/**
	 * 获取错误消息
	 *
	 * @return
	 */
	String getMessage();

	/**
	 * 调用结果
	 *
	 * @return
	 */
	Object getResult();

	/**
	 * 调用结果
	 *
	 * @param result
	 */
	void setResult(Object result);

	/**
	 * 获取错误
	 *
	 * @return
	 */
	Throwable getCause();
}
