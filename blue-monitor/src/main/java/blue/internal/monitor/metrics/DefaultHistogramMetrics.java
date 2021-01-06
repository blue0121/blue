package blue.internal.monitor.metrics;

import blue.core.util.NumberUtil;
import blue.core.util.StringUtil;
import blue.monitor.metrics.HistogramMetrics;
import io.prometheus.client.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Jin Zheng
 * @since 1.0 2019-10-30
 */
public class DefaultHistogramMetrics extends AbstractMetrics implements HistogramMetrics
{
	private static Logger logger = LoggerFactory.getLogger(DefaultHistogramMetrics.class);
	private static final double[] BUCKETS = {5, 10, 50, 100, 150, 200, 500, 1000, 2000, 5000};

	private Histogram histogram;
	private String buckets;

	public DefaultHistogramMetrics()
	{
	}

	@Override
	public void observeLatency(long millis, String...labels)
	{
		histogram.labels(labels).observe(millis);
	}

	@Override
	public long getSum(String...labels)
	{
		return (long) histogram.labels(labels).get().sum;
	}

	@Override
	public long getCount(String...labels)
	{
		double[] buckets = histogram.labels(labels).get().buckets;
		if (buckets == null || buckets.length == 0)
			return 0;

		return (long) buckets[buckets.length - 1];
	}

	@Override
	protected void initMetics()
	{
		String[] labelArr = StringUtil.split(labels).toArray(new String[0]);
		double[] bucketArr;
		if (buckets == null || buckets.isEmpty())
		{
			bucketArr = BUCKETS;
		}
		else
		{
			bucketArr = NumberUtil.splitDouble(buckets);
		}
		this.histogram = Histogram.build()
				.name(name)
				.help(help)
				.labelNames(labelArr)
				.buckets(bucketArr)
				.register(registry);
		logger.info("Initialize HistogramMetics, name: {}, label: {}, buckets: {}", name,
				Arrays.toString(labelArr), Arrays.toString(bucketArr));
	}

	public void setBuckets(String buckets)
	{
		this.buckets = buckets;
	}
}
