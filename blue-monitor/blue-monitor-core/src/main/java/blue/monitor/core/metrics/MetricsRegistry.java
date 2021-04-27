package blue.monitor.core.metrics;

import blue.monitor.internal.core.metrics.PrometheusRegistry;
import io.prometheus.client.CollectorRegistry;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-27
 */
public interface MetricsRegistry {

	/**
	 * 创建
	 * @param options
	 * @return
	 */
	static MetricsRegistry create(RegistryOptions options) {
		PrometheusRegistry registry = new PrometheusRegistry(options);
		registry.init();
		return registry;
	}

	CollectorRegistry getRegistry();

}
