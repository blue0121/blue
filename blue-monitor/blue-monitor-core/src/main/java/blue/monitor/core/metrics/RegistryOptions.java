package blue.monitor.core.metrics;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-27
 */
public class RegistryOptions {
    private String pushGateway;
    private long period;
    private String jobName;
    private String instance;

	public RegistryOptions() {
	}

    public String getPushGateway() {
        return pushGateway;
    }

    public RegistryOptions setPushGateway(String pushGateway) {
        this.pushGateway = pushGateway;
        return this;
    }

    public long getPeriod() {
        return period;
    }

    public RegistryOptions setPeriod(long period) {
        this.period = period;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public RegistryOptions setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public RegistryOptions setInstance(String instance) {
        this.instance = instance;
        return this;
    }
}
