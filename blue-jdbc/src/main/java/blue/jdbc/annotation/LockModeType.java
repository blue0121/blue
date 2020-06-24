package blue.jdbc.annotation;

/**
 * 锁模式
 * 
 * @author zhengj
 * @since 2016年7月6日 1.0
 */
public enum LockModeType
{
	/**
	 * 读锁
	 */
	READ,
	
	/**
	 * 写锁
	 */
	WRITE,
	
	/**
	 * 无锁
	 */
	NONE
}
