package blue.http.message;

/**
 * 会话用户监听器
 *
 * @author zhengjin
 * @since 1.0 2018年04月16日
 */
public interface SessionUserListener
{

	/**
	 * 会话用户强制下线
	 *
	 * @param token 上次用户会话令牌
	 * @param user 用户会话
	 */
	void fired(String token, SessionUser user);

}
