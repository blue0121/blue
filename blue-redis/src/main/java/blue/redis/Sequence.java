package blue.redis;

/**
 * @author Jin Zheng
 * @since 2020-07-05
 */
public interface Sequence
{

	String nextValue();

	void reset();

}
