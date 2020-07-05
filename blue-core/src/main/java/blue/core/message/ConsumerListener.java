package blue.core.message;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-28
 */
public interface ConsumerListener<T extends Topic, V>
{

	void onReceive(T topic, V message);

}
