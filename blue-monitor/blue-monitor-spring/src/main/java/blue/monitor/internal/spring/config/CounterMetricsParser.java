package blue.monitor.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.monitor.internal.spring.metrics.CounterMetricsFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * HistogramMetics解析器
 *
 * @author Jin Zheng
 * @date 2019-10-26
 */
public class CounterMetricsParser extends SimpleBeanDefinitionParser
{
	public CounterMetricsParser()
	{
		this.clazz = CounterMetricsFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "name", "name");
		this.parse(element, builder, "labels", "labels");
		this.parse(element, builder, "help", "help");
		this.parseRef(element, builder, "registry", "ref-metrics-registry");
	}

}
