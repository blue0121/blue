package blue.http.message;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2020-02-05
 */
public interface WebSocketRequest
{
	/**
	 * 获取令牌
	 *
	 * @return
	 */
	String getToken();

	/**
	 * 获取时间戳
	 *
	 * @return
	 */
	long getTimestamp();

	/**
	 * 获取URL
	 *
	 * @return
	 */
	String getUrl();

	/**
	 * 获取版本
	 *
	 * @return
	 */
	int getVersion();

	/**
	 * 获取对象并验证
	 *
	 * @param clazz
	 * @param groups
	 * @param <T>
	 * @return
	 */
	<T> T getObject(Class<T> clazz, Class<?>...groups);

	/**
	 * 获取对象列表并验证
	 *
	 * @param clazz
	 * @param groups
	 * @param <T>
	 * @return
	 */
	<T> List<T> getObjectList(Class<T> clazz, Class<?>...groups);

	/**
	 * 设置用户ID
	 *
	 * @param userId
	 */
	void setUserId(int userId);

	/**
	 * 获取用户ID
	 *
	 * @return
	 */
	int getUserId();

}
