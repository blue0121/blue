package blue.internal.monitor.metrics;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public interface HistogramMetrics
{

	void observeLatency(long millis, String...labels);

	long getSum(String...labels);

	long getCount(String...labels);

}
