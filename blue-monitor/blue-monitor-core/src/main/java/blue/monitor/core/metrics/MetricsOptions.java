package blue.monitor.core.metrics;

import io.prometheus.client.CollectorRegistry;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-25
 */
public class MetricsOptions {
	private CollectorRegistry registry;
	private String name;
	private String help;
	private String labels;
	private String buckets;

	public MetricsOptions() {

	}

	public CollectorRegistry getRegistry() {
		return registry;
	}

	public MetricsOptions setRegistry(CollectorRegistry registry) {
		this.registry = registry;
		return this;
	}

	public String getName() {
		return name;
	}

	public MetricsOptions setName(String name) {
		this.name = name;
		return this;
	}

	public String getHelp() {
		return help;
	}

	public MetricsOptions setHelp(String help) {
		this.help = help;
		return this;
	}

	public String getLabels() {
		return labels;
	}

	public MetricsOptions setLabels(String labels) {
		this.labels = labels;
		return this;
	}

	public String getBuckets() {
		return buckets;
	}

	public MetricsOptions setBuckets(String buckets) {
		this.buckets = buckets;
		return this;
	}
}
