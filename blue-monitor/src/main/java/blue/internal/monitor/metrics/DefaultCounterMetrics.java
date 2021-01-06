package blue.internal.monitor.metrics;

import blue.core.util.StringUtil;
import blue.monitor.metrics.CounterMetrics;
import io.prometheus.client.Counter;

/**
 * @author Jin Zheng
 * @since 1.0 2019-10-30
 */
public class DefaultCounterMetrics extends AbstractMetrics implements CounterMetrics
{

	private Counter counter;

	public DefaultCounterMetrics()
	{
	}

	@Override
	public void observeCount(long count, String...labels)
	{
		if (count <= 0)
			return;

		counter.labels(labels).inc(count);
	}

	@Override
	public void observeCount(String...labels)
	{
		this.observeCount(1L, labels);
	}

	@Override
	public long getCount(String...labels)
	{
		return (long) counter.labels(labels).get();
	}

	@Override
	protected void initMetics()
	{
		String[] labelArr = StringUtil.split(labels).toArray(new String[0]);
		this.counter = Counter.build()
				.name(name)
				.help(help)
				.labelNames(labelArr)
				.register(registry);
	}

}
