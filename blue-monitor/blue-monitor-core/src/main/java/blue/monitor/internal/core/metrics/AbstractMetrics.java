package blue.monitor.internal.core.metrics;

import blue.base.core.util.AssertUtil;
import blue.monitor.core.metrics.MetricsOptions;
import io.prometheus.client.CollectorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-10-30
 */
public abstract class AbstractMetrics {
	private static Logger logger = LoggerFactory.getLogger(AbstractMetrics.class);

	protected CollectorRegistry registry;
	protected String name;
	protected String help;
	protected String labels;
	protected String buckets;

	public AbstractMetrics(MetricsOptions options) {
		this.registry = options.getRegistry();
		this.name = options.getName();
		this.help = options.getHelp();
		this.labels = options.getLabels();
		this.buckets = options.getBuckets();
	}

	public void init() {
		if (registry == null) {
			logger.warn("CollectorRegistry is null, set default");
			registry = CollectorRegistry.defaultRegistry;
		}
		AssertUtil.notEmpty(name, "name");
		AssertUtil.notEmpty(labels, "label");
		this.initMetrics();
	}

	protected abstract void initMetrics();

	public CollectorRegistry getRegistry() {
		return registry;
	}
}
