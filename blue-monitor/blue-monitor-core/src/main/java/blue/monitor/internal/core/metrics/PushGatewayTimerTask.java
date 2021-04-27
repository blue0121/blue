package blue.monitor.internal.core.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TimerTask;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-27
 */
public class PushGatewayTimerTask extends TimerTask {
    private static Logger logger = LoggerFactory.getLogger(PushGatewayTimerTask.class);

    private final CollectorRegistry registry;
    private final String pushGateway;
    private final String jobName;
    private final Map<String, String> groupKey;

    public PushGatewayTimerTask(CollectorRegistry registry, String pushGateway,
                                String jobName, Map<String, String> groupKey) {
        this.registry = registry;
        this.pushGateway = pushGateway;
        this.jobName = jobName;
        this.groupKey = groupKey;
    }

    @Override
    public void run() {
        PushGateway gateway = new PushGateway(pushGateway);
        try {
            gateway.push(registry, jobName, groupKey);
            logger.debug("Push success, url: {}", pushGateway);
        }
        catch (Exception e) {
            logger.warn("Push error, url: {}, message: {}", pushGateway, e.getMessage());
        }
    }

}
