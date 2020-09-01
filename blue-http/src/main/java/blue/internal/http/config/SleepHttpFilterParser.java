package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.filter.SleepHttpFilter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2020-09-01
 */
public class SleepHttpFilterParser extends SimpleBeanDefinitionParser
{
	public SleepHttpFilterParser()
	{
		this.clazz = SleepHttpFilter.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "millis", "millis");
	}
}
