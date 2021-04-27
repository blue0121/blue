package blue.monitor.internal.core.metrics;

import blue.base.core.util.StringUtil;
import blue.monitor.core.metrics.CounterMetrics;
import blue.monitor.core.metrics.MetricsOptions;
import io.prometheus.client.Counter;

/**
 * @author Jin Zheng
 * @since 1.0 2019-10-30
 */
public class DefaultCounterMetrics extends AbstractMetrics implements CounterMetrics {

	private Counter counter;

	public DefaultCounterMetrics(MetricsOptions options) {
		super(options);
	}

	@Override
	public void inc(long count, String... labels) {
		if (count <= 0) {
			return;
		}

		counter.labels(labels).inc(count);
	}

	@Override
	public void inc(String... labels) {
		this.inc(1L, labels);
	}

	@Override
	public long getCount(String... labels) {
		return (long) counter.labels(labels).get();
	}

	@Override
	protected void initMetrics() {
		String[] labelArr = StringUtil.split(labels).toArray(new String[0]);
		this.counter = Counter.build()
				.name(name)
				.help(help)
				.labelNames(labelArr)
				.register(registry);
	}

}
