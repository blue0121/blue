package blue.internal.monitor.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.monitor.metrics.PrometheusRegistry;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * PrometheusRegistry解析器
 *
 * @author Jin Zheng
 * @date 2019-10-26
 */
public class PrometheusRegistryParser extends SimpleBeanDefinitionParser
{
	public PrometheusRegistryParser()
	{
		this.clazz = PrometheusRegistry.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "pushGateway", "push-gateway");
		this.parse(element, builder, "period", "period");
		this.parse(element, builder, "jobName", "job-name");
		this.parseRef(element, builder, "taskScheduler", "ref-task-scheduler");
	}

}
