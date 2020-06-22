package blue.monitor.metrics;

import blue.core.util.AssertUtil;
import io.prometheus.client.CollectorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2019-10-30
 */
public abstract class AbstractMetrics implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(AbstractMetrics.class);

	protected CollectorRegistry registry;
	protected String name;
	protected String help;
	protected String labels;

	public AbstractMetrics()
	{
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (registry == null)
		{
			logger.warn("CollectorRegistry is null, set default");
			registry = CollectorRegistry.defaultRegistry;
		}
		AssertUtil.notEmpty(name, "name");
		AssertUtil.notEmpty(labels, "label");
		this.initMetics();
	}

	protected abstract void initMetics();

	public void setRegistry(CollectorRegistry registry)
	{
		this.registry = registry;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setHelp(String help)
	{
		this.help = help;
	}

	public void setLabels(String labels)
	{
		this.labels = labels;
	}
}
