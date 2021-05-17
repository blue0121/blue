package blue.redis.core.options;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public enum RedisSequenceMode {

	/**
	 * 原子自增
	 */
	ATOMIC,

	/**
	 * 带日期原子自增
	 */
	DATE,

}
