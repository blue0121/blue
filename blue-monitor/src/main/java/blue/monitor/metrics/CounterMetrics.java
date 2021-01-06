package blue.monitor.metrics;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public interface CounterMetrics
{

	void observeCount(long count, String...labels);

	void observeCount(String...labels);

	long getCount(String...labels);

}
