package blue.monitor.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.monitor.internal.spring.metrics.MetricsRegistryFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * PrometheusRegistry解析器
 *
 * @author Jin Zheng
 * @date 2019-10-26
 */
public class MetricsRegistryParser extends SimpleBeanDefinitionParser
{
	public MetricsRegistryParser()
	{
		this.clazz = MetricsRegistryFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "pushGateway", "push-gateway");
		this.parse(element, builder, "period", "period");
		this.parse(element, builder, "jobName", "job-name");
		this.parse(element, builder, "instance", "instance");
	}

}
