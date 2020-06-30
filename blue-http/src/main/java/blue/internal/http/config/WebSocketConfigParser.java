package blue.internal.http.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketConfigParser extends FilterConfigDefinitionParser
{
	public WebSocketConfigParser()
	{
		this.clazz = WebSocketConfig.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parseFilterConfig(element, builder.getRawBeanDefinition());
	}
}
