package blue.monitor.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Jin Zheng
 * @since 1.0 2019-10-29
 */
public class PrometheusRegistry implements InitializingBean, DisposableBean, FactoryBean<CollectorRegistry>
{
	private static Logger logger = LoggerFactory.getLogger(PrometheusRegistry.class);

	private String pushGateway;
	private long period;
	private String jobName;
	private CollectorRegistry registry;
	private TaskScheduler taskScheduler;
	private ScheduledFuture<?> scheduledFuture;
	private Map<String, String> groupKey;

	public PrometheusRegistry()
	{
		this.registry = new CollectorRegistry(true);
		this.groupKey = new HashMap<>();
		this.groupKey.put("instance", UUID.randomUUID().toString());
	}

	@Override
	public CollectorRegistry getObject() throws Exception
	{
		return registry;
	}

	@Override
	public Class<?> getObjectType()
	{
		return CollectorRegistry.class;
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (pushGateway == null || pushGateway.isEmpty())
		{
			logger.warn("push-gateway is empty, Not start");
			return;
		}
		if (taskScheduler == null)
		{
			logger.warn("task-scheduler is null, Not start");
			return;
		}
		if (period <= 0)
		{
			logger.warn("period less than 0, {}, Not start", period);
			return;
		}

		scheduledFuture = taskScheduler.scheduleAtFixedRate(() -> this.initScheduler(), period);
		logger.info("Start PrometheusRegistry scheduler, jobName: {}, period: {}ms, url: {}, groupingKey: {}",
				jobName, period, pushGateway, groupKey);
	}

	private void initScheduler()
	{
		PushGateway gateway = new PushGateway(pushGateway);
		try
		{
			gateway.push(registry, jobName, groupKey);
			logger.debug("Push success, url: {}", pushGateway);
		}
		catch (Exception e)
		{
			logger.warn("Push error, url: {}, message: {}", pushGateway, e.getMessage());
		}
	}

	@Override
	public void destroy() throws Exception
	{
		if (scheduledFuture != null)
		{
			scheduledFuture.cancel(false);
		}
	}

	public CollectorRegistry getRegistry()
	{
		return registry;
	}

	public void setPushGateway(String pushGateway)
	{
		this.pushGateway = pushGateway;
	}

	public void setTaskScheduler(TaskScheduler taskScheduler)
	{
		this.taskScheduler = taskScheduler;
	}

	public void setPeriod(long period)
	{
		this.period = period;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}
}
