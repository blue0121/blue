package blue.monitor.internal.core.metrics;

import blue.base.core.id.IdGenerator;
import blue.monitor.core.metrics.MetricsRegistry;
import blue.monitor.core.metrics.RegistryOptions;
import io.prometheus.client.CollectorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Jin Zheng
 * @since 1.0 2019-10-29
 */
public class PrometheusRegistry implements MetricsRegistry {
	private static Logger logger = LoggerFactory.getLogger(PrometheusRegistry.class);
	private static final String INSTANCE = "instance";

	private String pushGateway;
	private long period;
	private String jobName;
	private String instance;

	private CollectorRegistry registry;
	private Timer timer;
	private Map<String, String> groupKey;

	public PrometheusRegistry(RegistryOptions options) {
		this.pushGateway = options.getPushGateway();
		this.period = options.getPeriod();
		this.jobName = options.getJobName();
		this.instance = options.getInstance();

		this.registry = new CollectorRegistry(true);
		this.groupKey = new HashMap<>();
		this.timer = new Timer(this.getClass().getSimpleName(), true);
	}

	public void init() {
		if (pushGateway == null || pushGateway.isEmpty()) {
			logger.warn("push-gateway is empty, Not start");
			return;
		}
		if (period <= 0) {
			logger.warn("period less than 0, {}, Not start", period);
			return;
		}
		if (instance == null || instance.isEmpty()) {
			this.groupKey.put(INSTANCE, IdGenerator.uuid12bit());
		} else {
			this.groupKey.put(INSTANCE, instance);
		}

		TimerTask timerTask = new PushGatewayTimerTask(registry, pushGateway, jobName, groupKey);
		timer.scheduleAtFixedRate(timerTask, 0, period);
		logger.info("Start PrometheusRegistry scheduler, jobName: {}, period: {}ms, url: {}, groupingKey: {}",
				jobName, period, pushGateway, groupKey);
	}


	public void destroy() throws Exception {
		timer.cancel();
	}

	@Override
	public CollectorRegistry getRegistry() {
		return registry;
	}
}
