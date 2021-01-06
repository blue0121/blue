package blue.internal.monitor.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.monitor.metrics.DefaultHistogramMetrics;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * HistogramMetics解析器
 *
 * @author Jin Zheng
 * @date 2019-10-26
 */
public class HistogramMetricsParser extends SimpleBeanDefinitionParser
{
	public HistogramMetricsParser()
	{
		this.clazz = DefaultHistogramMetrics.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "name", "name");
		this.parse(element, builder, "labels", "labels");
		this.parse(element, builder, "help", "help");
		this.parse(element, builder, "buckets", "buckets");
		this.parseRef(element, builder, "registry", "ref-registry");
	}

}
