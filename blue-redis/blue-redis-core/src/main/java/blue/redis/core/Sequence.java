package blue.redis.core;

/**
 * @author Jin Zheng
 * @since 2020-07-05
 */
public interface Sequence {

	/**
	 * 下一个值
	 *
	 * @return
	 */
	String nextValue();

	/**
	 * 重置
	 */
	void reset();

}
