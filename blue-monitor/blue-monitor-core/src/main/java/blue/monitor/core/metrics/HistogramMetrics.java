package blue.monitor.core.metrics;

import blue.monitor.internal.core.metrics.DefaultHistogramMetrics;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public interface HistogramMetrics {

	/**
	 * 创建柱状图
	 * @param options
	 * @return
	 */
	static HistogramMetrics create(MetricsOptions options) {
		DefaultHistogramMetrics histogram = new DefaultHistogramMetrics(options);
		histogram.init();
		return histogram;
	}

	void observe(long millis, String... labels);

	long getSum(String... labels);

	long getCount(String... labels);

}
