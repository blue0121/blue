package test.monitor.metrics;

import blue.internal.monitor.metrics.CounterMetrics;
import blue.internal.monitor.metrics.HistogramMetrics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
@SpringJUnitConfig(locations = {"classpath:spring/monitor.xml"})
public class MetricsTest
{
	private HistogramMetrics histogramMetics;
	private CounterMetrics counterMetics;

	private String label = "label";

	public MetricsTest()
	{
	}

	@Test
	public void testHistogram()
	{
		histogramMetics.observeLatency(100L, label);
		Assertions.assertEquals(100L, histogramMetics.getSum(label));
		Assertions.assertEquals(1L, histogramMetics.getCount(label));
	}

	@Test
	public void testCounter()
	{
		counterMetics.observeCount(label);
		Assertions.assertEquals(1L, counterMetics.getCount(label));
	}

	@Autowired
	public void setHistogramMetics(HistogramMetrics histogramMetics)
	{
		this.histogramMetics = histogramMetics;
	}

	@Autowired
	public void setCounterMetics(CounterMetrics counterMetics)
	{
		this.counterMetics = counterMetics;
	}
}
