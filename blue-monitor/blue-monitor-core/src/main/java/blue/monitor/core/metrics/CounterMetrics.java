package blue.monitor.core.metrics;

import blue.monitor.internal.core.metrics.DefaultCounterMetrics;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public interface CounterMetrics {

	/**
	 * 创建计数器
	 * @param options
	 * @return
	 */
	static CounterMetrics create(MetricsOptions options) {
		DefaultCounterMetrics counter = new DefaultCounterMetrics(options);
		counter.init();
		return counter;
	}

	/**
	 * increase count
	 * @param count
	 * @param labels
	 */
	void inc(long count, String... labels);

	/**
	 * increase 1 => inc(1, labels)
	 * @param labels
	 */
	void inc(String... labels);

	/**
	 * get current count
	 * @param labels
	 * @return
	 */
	long getCount(String... labels);

}
