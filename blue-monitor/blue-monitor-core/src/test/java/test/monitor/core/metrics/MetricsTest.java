package test.monitor.core.metrics;

import blue.monitor.core.metrics.CounterMetrics;
import blue.monitor.core.metrics.HistogramMetrics;
import blue.monitor.core.metrics.MetricsOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public class MetricsTest {

	private String label = "label";

	public MetricsTest() {
	}

	@Test
	public void testHistogram() {
		MetricsOptions options = new MetricsOptions();
		options.setName("histogram");
		options.setHelp("histogram");
		options.setLabels(label);
		HistogramMetrics histogramMetrics = HistogramMetrics.create(options);

		histogramMetrics.observe(100L, label);
		Assertions.assertEquals(100L, histogramMetrics.getSum(label));
		Assertions.assertEquals(1L, histogramMetrics.getCount(label));
	}

	@Test
	public void testCounter() {
		MetricsOptions options = new MetricsOptions();
		options.setName("counter");
		options.setHelp("counter");
		options.setLabels(label);
		CounterMetrics counterMetrics = CounterMetrics.create(options);

		counterMetrics.inc(label);
		Assertions.assertEquals(1L, counterMetrics.getCount(label));
	}
}
