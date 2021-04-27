package blue.monitor.internal.spring.metrics;

import blue.monitor.core.metrics.MetricsRegistry;
import blue.monitor.core.metrics.RegistryOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-27
 */
public class MetricsRegistryFactoryBean implements FactoryBean<MetricsRegistry>, InitializingBean {
    private String pushGateway;
    private long period;
    private String jobName;
    private String instance;

    private MetricsRegistry registry;

	public MetricsRegistryFactoryBean() {
	}

    @Override
    public MetricsRegistry getObject() throws Exception {
        return registry;
    }

    @Override
    public Class<?> getObjectType() {
        return MetricsRegistry.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RegistryOptions options = new RegistryOptions();
        options.setPushGateway(pushGateway)
                .setPeriod(period)
                .setJobName(jobName)
                .setInstance(instance);
        this.registry = MetricsRegistry.create(options);
    }

    public void setPushGateway(String pushGateway) {
        this.pushGateway = pushGateway;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}
