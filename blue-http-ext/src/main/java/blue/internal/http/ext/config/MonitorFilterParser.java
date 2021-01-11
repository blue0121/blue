package blue.internal.http.ext.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.ext.management.MonitorFilter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-02
 */
public class MonitorFilterParser extends SimpleBeanDefinitionParser
{
	public MonitorFilterParser()
	{
		this.clazz = MonitorFilter.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "buckets", "buckets");
	}
}
