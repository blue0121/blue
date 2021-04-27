package test.monitor.spring.metrics;

import blue.monitor.core.metrics.CounterMetrics;
import blue.monitor.core.metrics.HistogramMetrics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
@SpringJUnitConfig(locations = {"classpath:spring/monitor.xml"})
public class MetricsSpringTest
{
	private HistogramMetrics histogramMetrics;
	private CounterMetrics counterMetrics;

	private String label = "label";

	public MetricsSpringTest()
	{
	}

	@Test
	public void testHistogram()
	{
		histogramMetrics.observe(100L, label);
		Assertions.assertEquals(100L, histogramMetrics.getSum(label));
		Assertions.assertEquals(1L, histogramMetrics.getCount(label));
	}

	@Test
	public void testCounter()
	{
		counterMetrics.inc(label);
		Assertions.assertEquals(1L, counterMetrics.getCount(label));
	}

	@Autowired
	public void setHistogramMetrics(HistogramMetrics histogramMetrics)
	{
		this.histogramMetrics = histogramMetrics;
	}

	@Autowired
	public void setCounterMetrics(CounterMetrics counterMetrics)
	{
		this.counterMetrics = counterMetrics;
	}
}
