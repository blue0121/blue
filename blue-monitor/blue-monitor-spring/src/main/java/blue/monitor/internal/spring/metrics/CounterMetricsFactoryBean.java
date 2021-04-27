package blue.monitor.internal.spring.metrics;

import blue.monitor.core.metrics.CounterMetrics;
import blue.monitor.core.metrics.MetricsOptions;
import blue.monitor.core.metrics.MetricsRegistry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-27
 */
public class CounterMetricsFactoryBean implements FactoryBean<CounterMetrics>, InitializingBean {
    private MetricsRegistry registry;
    private String name;
    private String help;
    private String labels;

    private CounterMetrics metrics;

	public CounterMetricsFactoryBean() {
	}

    @Override
    public CounterMetrics getObject() throws Exception {
        return metrics;
    }

    @Override
    public Class<?> getObjectType() {
        return CounterMetrics.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MetricsOptions options = new MetricsOptions();
        options.setRegistry(registry.getRegistry())
                .setName(name)
                .setHelp(help)
                .setLabels(labels);
        this.metrics = CounterMetrics.create(options);
    }

    public void setRegistry(MetricsRegistry registry) {
        this.registry = registry;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }
}
