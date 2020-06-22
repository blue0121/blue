package blue.internal.monitor.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public class MonitorNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/monitor";

	public MonitorNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("prometheus-registry", new PrometheusRegistryParser());
		this.registerBeanDefinitionParser("histogram-metrics", new HistogramMetricsParser());
		this.registerBeanDefinitionParser("counter-metrics", new CounterMetricsParser());
	}
}
