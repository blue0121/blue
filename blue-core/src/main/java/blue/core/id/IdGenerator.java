package blue.core.id;

import blue.internal.core.id.UUID;

/**
 * 不重复 ID 产生器
 * 
 * @author zhengj
 * @since 1.0 2017年8月11日
 */
public class IdGenerator
{
	private IdGenerator()
	{
	}
	
	/**
	 * UUID－32位长度，有序
	 */
	public static String uuid32bit()
	{
		return UUID.generator();
	}
	
	/**
	 * UUID－12至13位长度，有序
	 */
	public static String uuid12bit()
	{
		return Long.toHexString(SnowflakeIdFactory.getSingleSnowflakeId().nextId());
	}
	
	/**
	 * long 类型，有序
	 */
	public static long id()
	{
		return SnowflakeIdFactory.getSingleSnowflakeId().nextId();
	}
	
}
